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

import com.gmail.blueboxware.libgdxplugin.utils.isProfilingCall
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.kotlin.psi.KtQualifiedExpression
import org.jetbrains.kotlin.psi.KtVisitorVoid
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

class KotlinProfilingCodeInspection: LibGDXKotlinBaseInspection() {

  override fun getStaticDescription() = message("profiling.code.html.description")

  override fun getID() = "LibGDXProfilingCode"

  override fun getDisplayName() = message("profiling.code.inspection.name")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: KtVisitorVoid() {

    override fun visitQualifiedExpression(expression: KtQualifiedExpression) {

      val (receiverType, methodName) = LibGDXKotlinBaseInspection.resolveMethodCallExpression(expression) ?: return
      val className = receiverType.fqNameSafe.asString()

      if (isProfilingCall(className, methodName)) {
        holder.registerProblem(expression, message("profiling.code.problem.descriptor"))
      }

    }
    
  }

}
