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

import com.gmail.blueboxware.libgdxplugin.inspections.utils.isLibGDXProject
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.ConstructorDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.idea.intentions.calleeName
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtQualifiedExpression
import org.jetbrains.kotlin.resolve.bindingContextUtil.getReferenceTargets
import org.jetbrains.kotlin.resolve.calls.callUtil.getCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.types.KotlinType

open class LibGDXKotlinBaseInspection: AbstractKotlinInspection() {

  override fun getGroupPath() = arrayOf("LibGDX", "Kotlin")

  override fun getGroupDisplayName() = "LibGDX"

  override fun isEnabledByDefault() = true

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.WARNING

  override val suppressionKey = id

  override fun isSuppressedFor(element: PsiElement): Boolean {
      return !isLibGDXProject(element.project) || super.isSuppressedFor(element)
  }

  companion object {
    fun resolveMethodCallExpression(expression: KtQualifiedExpression): Pair<DeclarationDescriptor, String>? {

      var receiverType: DeclarationDescriptor? = expression.analyze().getType(expression.receiverExpression)?.constructor?.declarationDescriptor

      if (receiverType == null) {
        // static method call?
        receiverType = expression.receiverExpression.getReferenceTargets(expression.analyze()).firstOrNull() ?: return null
      }

      val methodName = expression.calleeName ?: return null

      return Pair(receiverType, methodName)

    }

    fun getClassIfConstructorCall(expression: KtCallExpression): KotlinType? {

      val context = expression.analyze()
      val descriptor = expression.getCall(context)?.getResolvedCall(context)?.candidateDescriptor

      if (descriptor is ConstructorDescriptor) {
        return descriptor.returnType
      }

      return null
    }
  }



}
