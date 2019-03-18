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

import com.gmail.blueboxware.libgdxplugin.inspections.getFlushingMethods
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*
import org.jetbrains.kotlin.asJava.classes.KtLightClass
import org.jetbrains.kotlin.asJava.elements.KtLightMethod
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPropertyAccessor

class JavaFlushInsideLoopInspection: LibGDXJavaBaseInspection() {

  override fun getStaticDescription() = message("flushing.inside.loop.html.description")

  override fun getID() = "LibGDXFlushInsideLoop"

  override fun getDisplayName() = message("flushing.inside.loop.inspection.name")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean, session: LocalInspectionToolSession) = object: JavaElementVisitor() {

    override fun visitMethod(method: PsiMethod?) {
      if (method == null) return

      val statements = method.body?.statements ?: return

      for (statement in statements) {
        (statement as? PsiLoopStatement)?.accept(LoopChecker(holder, session))
      }

    }

  }
}

private class LoopChecker(val holder: ProblemsHolder, session: LocalInspectionToolSession): JavaRecursiveElementVisitor() {

  val allFlushingMethods = getFlushingMethods(holder.project, session)

  override fun visitCallExpression(callExpression: PsiCallExpression?) {

    if (allFlushingMethods == null || callExpression == null) return

    val method = callExpression.resolveMethod() ?: return

    if (allFlushingMethods.contains(method)) {
      registerProblem(callExpression)
    } else if (method is KtLightMethod) {
      val originalElement = method.kotlinOrigin ?: return
      if (originalElement is KtProperty) {
        var accessor: KtPropertyAccessor? = null
        if (method.name.startsWith("get")) {
          accessor = originalElement.getter
        } else if (method.name.startsWith("set")) {
          accessor = originalElement.setter
        }
        if (accessor != null && allFlushingMethods.contains(accessor)) {
          registerProblem(callExpression)
        }
      } else {
        if (allFlushingMethods.contains(originalElement)) {
          registerProblem(callExpression)
        }
      }
    }
  }

  override fun visitNewExpression(expression: PsiNewExpression?) {
    if (expression == null || allFlushingMethods == null) return

    val method = expression.resolveMethod()

    if (method != null) {

      if (allFlushingMethods.contains(method)) {
        registerProblem(expression)
        return
      }
      if (method is KtLightMethod) {
        method.kotlinOrigin?.let { kotlinOrigin ->
          if (allFlushingMethods.contains(kotlinOrigin)) {
            registerProblem(expression)
          }
        }
      }

      val clazz = expression.classReference?.resolve()

      if (clazz is KtLightClass) {
        val initializers = clazz.kotlinOrigin?.getAnonymousInitializers() ?: return
        for (initializer in initializers) {
          if (allFlushingMethods.contains(initializer)) {
            registerProblem(expression)
            break
          }
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