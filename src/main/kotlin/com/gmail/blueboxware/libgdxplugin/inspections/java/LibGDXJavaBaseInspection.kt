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

import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectComponent
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.BaseJavaLocalInspectionTool
import com.intellij.psi.*

open class LibGDXJavaBaseInspection : BaseJavaLocalInspectionTool() {

  override fun getGroupPath() = arrayOf("LibGDX", "Java")

  override fun getGroupDisplayName() = "LibGDX"

  override fun isEnabledByDefault() = true

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.WARNING

  override fun isSuppressedFor(element: PsiElement): Boolean {
    return !(element.project.getComponent(LibGDXProjectComponent::class.java)?.isLibGDXProject ?: false) || super.isSuppressedFor(element)
  }

  companion object {

    fun resolveMethodCallExpression(expression: PsiMethodCallExpression): Pair<PsiClass, String>? {

      val qualifierExpression = expression.methodExpression.qualifierExpression ?: return null

      var receiverType = (qualifierExpression.type as? PsiClassType)?.resolve()

      if (receiverType == null) {
        // static method call?
        receiverType = (qualifierExpression as? PsiReference)?.resolve() as? PsiClass ?: return null
      }

      val methodName = expression.resolveMethod()?.name ?: return null

      return Pair(receiverType, methodName)
    }
  }
}
