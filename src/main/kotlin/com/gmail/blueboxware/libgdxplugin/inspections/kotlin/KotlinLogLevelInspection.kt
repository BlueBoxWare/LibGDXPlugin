package com.gmail.blueboxware.libgdxplugin.inspections.kotlin

import com.gmail.blueboxware.libgdxplugin.utils.isSetLogLevel
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.idea.references.SyntheticPropertyAccessorReference
import org.jetbrains.kotlin.lexer.KtSingleValueToken
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.calls.callUtil.getCalleeExpressionIfAny

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
class KotlinLogLevelInspection: LibGDXKotlinBaseInspection() {

  override fun getStaticDescription() = message("log.level.html.description")

  override fun getID() = "LibGDXLogLevel"

  override fun getDisplayName() = message("log.level.inspection")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: KtVisitorVoid() {

    override fun visitCallExpression(expression: KtCallExpression) {

      val refs = expression.calleeExpression?.references ?: return

      for (ref in refs) {
        val target = ref.resolve() ?: continue
        if (target is PsiMethod) {
          val clazz = target.containingClass ?: continue
          val methodName = expression.calleeExpression?.text ?: continue
          if (isSetLogLevel(clazz, methodName)) {

            val argument = expression.valueArgumentList?.arguments?.firstOrNull()?.getArgumentExpression() ?: return

            if (isLogLevelArgument(argument)) {
              holder.registerProblem(expression, message("log.level.problem.descriptor"))
            }

          }
        }
      }

    }

    override fun visitQualifiedExpression(expression: KtQualifiedExpression) {

      (expression.context as? KtBinaryExpression)?.let { context ->

        val operator = (context.operationToken as? KtSingleValueToken)?.value ?: return

        if (operator != "=") return

        val refs = expression.selectorExpression?.references ?: return

        for (ref in refs) {

          if (ref is SyntheticPropertyAccessorReference.Setter) {
            val target = ref.resolve()
            if (target is PsiMethod) {
              val clazz = target.containingClass ?: continue
              val methodName = target.name

              if (isSetLogLevel(clazz, methodName)) {

                val argument = context.right ?: continue
                if (isLogLevelArgument(argument)) {
                  holder.registerProblem(context, message("log.level.problem.descriptor"))
                }

              }
            }
          }

        }
      }

    }
  }

}

private fun isLogLevelArgument(expression: KtExpression?): Boolean {

  if (expression is KtConstantExpression && (expression.text == "3" || expression.text == "4")) {
    return true
  } else if (expression is KtDotQualifiedExpression) {
    val refs = expression.getCalleeExpressionIfAny()?.references ?: return false
    for (ref in refs) {

      val target = ref.resolve()?.getKotlinFqName()?.asString() ?: continue
      if (
      target == "com.badlogic.gdx.Application.LOG_DEBUG"
              || target == "com.badlogic.gdx.Application.LOG_INFO"
              || target == "com.badlogic.gdx.utils.Logger.DEBUG"
              || target == "com.badlogic.gdx.utils.Logger.INFO"
      ) {

        return true

      }

    }
  }

  return false
}