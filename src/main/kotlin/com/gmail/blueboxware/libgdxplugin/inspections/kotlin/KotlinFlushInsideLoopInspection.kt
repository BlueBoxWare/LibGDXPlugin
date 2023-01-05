/*
 * Copyright 2016 Blue Box Ware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gmail.blueboxware.libgdxplugin.inspections.kotlin

import com.gmail.blueboxware.libgdxplugin.inspections.getFlushingMethods
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.idea.references.KtInvokeFunctionReference
import org.jetbrains.kotlin.idea.references.SyntheticPropertyAccessorReference
import org.jetbrains.kotlin.psi.*

class KotlinFlushInsideLoopInspection : LibGDXKotlinBaseInspection() {

    override fun getStaticDescription() = message("flushing.inside.loop.html.description")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean, session: LocalInspectionToolSession) =
        object : KtVisitorVoid() {

            override fun visitBlockExpression(expression: KtBlockExpression) {

                for (statement in expression.statements) {
                    (statement as? KtLoopExpression)?.accept(LoopBodyChecker(holder, session))
                }

            }

        }
}

private class LoopBodyChecker(val holder: ProblemsHolder, session: LocalInspectionToolSession) : KtTreeVisitorVoid() {

    val allFlushingMethods = getFlushingMethods(holder.project, session)

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        if (allFlushingMethods == null) return

        val refs = expression.calleeExpression?.references ?: return

        for (ref in refs) {
            var target = ref.resolve()

            if (allFlushingMethods.contains(target)) {
                registerProblem(expression)
                break
            }

            //
            // If target is KtConstructor or KtClass, check if the init blocks of the target class are flushing methods
            //

            if (target is KtConstructor<*>) {
                target = target.getContainingClassOrObject()
            }

            if (target is KtClass) {
                val initializers = target.getAnonymousInitializers()
                for (initializer in initializers) {
                    if (allFlushingMethods.contains(initializer)) {
                        registerProblem(expression)
                        break
                    }
                }
            }

            //
            // Handle function properties like
            //      val f = { SpriteBatch.flush() }
            // and
            //      val f = fun() { SpriteBatch.flush() }
            //

            if (target is KtProperty) {
                val initializer = target.initializer
                if (initializer is KtNamedFunction && allFlushingMethods.contains(initializer)) {
                    registerProblem(expression)
                    break
                } else if (initializer is KtLambdaExpression && allFlushingMethods.contains(initializer.functionLiteral)) {
                    registerProblem(expression)
                    break
                }
            }

            //
            // Handle indirect function calls like f()() where f() returns a function
            // ex. fun f(): ()->Unit  = { SpriteBatch().flush() }
            //

            if (ref is KtInvokeFunctionReference) {
                val element = ref.element
                val moreRefs = element.calleeExpression?.references ?: continue
                for (nextRef in moreRefs) {
                    val functionBody = (nextRef.resolve() as? KtFunction)?.bodyExpression ?: continue
                    if (functionBody is KtLambdaExpression) {
                        val functionLiteral = functionBody.functionLiteral
                        if (allFlushingMethods.contains(functionLiteral)) {
                            registerProblem(expression)
                            break
                        }
                    }
                }

            }

        }
    }

    override fun visitDeclaration(dcl: KtDeclaration) {
        if (dcl !is KtFunction) {
            super.visitDeclaration(dcl)
        }
    }

    override fun visitExpression(expression: KtExpression) {
        if (expression !is KtLambdaExpression || expression.context is KtValueArgument) {
            super.visitExpression(expression)
        }
    }

    override fun visitQualifiedExpression(expression: KtQualifiedExpression) {
        super.visitQualifiedExpression(expression)

        if (allFlushingMethods == null) return

        val refs = expression.selectorExpression?.references ?: return

        //
        // Are we using a property accessor which contains a flushing call?
        //
        val spars = refs.filterIsInstance<SyntheticPropertyAccessorReference>()

        if (spars.isEmpty()) {
            return
        }

        val isGetter = spars.any { it.getter }

        for (ref in refs) {
            val target = ref.resolve()
            if (target is KtProperty) {
                // Kotlin accessor
                val accessor = if (isGetter) target.getter else target.setter
                if (accessor != null && allFlushingMethods.contains(accessor)) {
                    registerProblem(expression)
                    return
                }
            } else if (target is PsiMethod) {
                // Java setter or getter
                if (allFlushingMethods.contains(target)) {
                    registerProblem(expression)
                    return
                }
            }
        }

    }

    fun registerProblem(expression: PsiElement) {
        for (problem in holder.results) {
            if (problem.psiElement == expression) return
        }

        holder.registerProblem(expression, message("flushing.inside.loop.problem.descriptor"))
    }
}
