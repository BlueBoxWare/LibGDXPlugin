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
import com.gmail.blueboxware.libgdxplugin.utils.isProfilingCall
import com.gmail.blueboxware.libgdxplugin.utils.resolveCall
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiMethodCallExpression


class JavaProfilingCodeInspection: LibGDXJavaBaseInspection() {

  override fun getStaticDescription() = message("profiling.code.html.description")

  override fun getID() = "LibGDXProfilingCode"

  override fun getDisplayName() = message("profiling.code.inspection.name")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: JavaElementVisitor() {

    override fun visitMethodCallExpression(expression: PsiMethodCallExpression?) {
      super.visitMethodCallExpression(expression)

      if (expression == null) return

      val (receiverClass, method) = expression.resolveCall() ?: return
      val className = receiverClass.qualifiedName ?: return

      if (isProfilingCall(className, method.name)) {
        holder.registerProblem(expression, message("profiling.code.problem.descriptor"))
      }
    }

  }

}