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

import com.gmail.blueboxware.libgdxplugin.utils.compat.getCall
import com.gmail.blueboxware.libgdxplugin.utils.compat.getResolvedCall
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDXProject
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.ConstructorDescriptor
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.types.KotlinType

@Suppress("InspectionDescriptionNotFoundInspection")
open class LibGDXKotlinBaseInspection : LocalInspectionTool() {

    override fun isSuppressedFor(element: PsiElement): Boolean {
        return !element.project.isLibGDXProject() || super.isSuppressedFor(element)
    }

    companion object {

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
