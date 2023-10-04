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
package com.gmail.blueboxware.libgdxplugin.inspections.java

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.iteratorsMap
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*

internal class JavaUnsafeIteratorInspection : LibGDXJavaBaseInspection() {

    override fun getStaticDescription() = message("unsafeiterator.html.description")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object : JavaElementVisitor() {

        override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
            super.visitMethodCallExpression(expression)

            val qualifierExpression = expression.methodExpression.qualifierExpression ?: return

            if (qualifierExpression is PsiNewExpression) return

            val receiverType = qualifierExpression.type as? PsiClassType ?: return

            val receiverClass = receiverType.resolve() ?: return
            val receiverFqClassName = receiverClass.qualifiedName ?: return
            val receiverShortClassName = receiverClass.name

            if (!iteratorsMap.containsKey(receiverFqClassName)) return

            val methodName = expression.resolveMethod()?.name ?: return

            if (iteratorsMap[receiverFqClassName]?.contains(methodName) == true) {

                holder.registerProblem(
                    expression,
                    "${message("unsafeiterator.problem.descriptor")}: $receiverShortClassName.$methodName()"
                )

            }
        }

        override fun visitForeachStatement(statement: PsiForeachStatement) {
            super.visitForeachStatement(statement)

            val receiverType = (statement.iteratedValue?.type ?: return) as? PsiClassType ?: return

            val receiverClass = receiverType.resolve() ?: return
            val receiverFqClassName = receiverClass.qualifiedName
            val receiverShortClassName = receiverClass.name

            if (!iteratorsMap.containsKey(receiverFqClassName)
                || iteratorsMap[receiverFqClassName]?.contains("iterator") != true
            ) return

            statement.iteratedValue?.let { iteratedValue ->
                holder.registerProblem(
                    iteratedValue,
                    "${message("unsafeiterator.problem.descriptor")}: $receiverShortClassName.iterator()"
                )
            }

        }
    }

}

