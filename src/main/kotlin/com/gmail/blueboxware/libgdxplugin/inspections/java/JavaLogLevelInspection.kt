package com.gmail.blueboxware.libgdxplugin.inspections.java

import com.gmail.blueboxware.libgdxplugin.inspections.utils.isSetLogLevel
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiLiteral
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.impl.compiled.ClsFieldImpl

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

class JavaLogLevelInspection: LibGDXJavaBaseInspection() {

  override fun getStaticDescription() = message("log.level.html.description")

  override fun getID() = "LibGDXLogLevel"

  override fun getDisplayName() = message("log.level.inspection")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: JavaElementVisitor() {

    override fun visitMethodCallExpression(expression: PsiMethodCallExpression?) {

      if (expression == null) return

      val (receiverClass, methodName) = LibGDXJavaBaseInspection.resolveMethodCallExpression(expression) ?: return

      if (isSetLogLevel(receiverClass, methodName)) {
        val argument = expression.argumentList.expressions.firstOrNull() ?: return

        if (argument is PsiReferenceExpression) {
          val resolved = argument.resolve()
          if (resolved is ClsFieldImpl) {
            if (resolved.containingClass?.qualifiedName == "com.badlogic.gdx.Application" && (resolved.name == "LOG_DEBUG" || resolved.name == "LOG_INFO")) {
              holder.registerProblem(expression, message("log.level.problem.descriptor"))
            }
          }
        } else if (argument is PsiLiteral) {
          val value = argument.value
          if (value == 2 || value == 3) {
            holder.registerProblem(expression, message("log.level.problem.descriptor"))
          }
        }

      }

    }

  }
}
