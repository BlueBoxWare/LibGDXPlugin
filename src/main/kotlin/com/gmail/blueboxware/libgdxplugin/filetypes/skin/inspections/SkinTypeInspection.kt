package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.getRealClassNamesAsString
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.PsiTypes

/*
 * Copyright 2017 Blue Box Ware
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
internal class SkinTypeInspection : SkinBaseInspection() {

    override fun getStaticDescription() = message("skin.inspection.types.description")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : SkinElementVisitor() {

            override fun visitValue(skinValue: SkinValue) {

                fun problem(expectedType: String) {
                    holder.registerProblem(skinValue, message("skin.inspection.types.type.expected", expectedType))
                }

                if (skinValue.parent is SkinPropertyName
                    || skinValue.parent is SkinClassName
                    || skinValue.parent is SkinResourceName
                ) {
                    return
                }

                val expectedType = skinValue.resolveToType()
                val property = skinValue.property
                val containingClassName = property?.containingObject?.resolveToClass()?.qualifiedName
                val propertyName = property?.propertyName?.value

                if (expectedType?.componentType(skinValue.project) != null) {
                    if (skinValue !is SkinArray) {
                        problem("Array")
                    }
                } else if (expectedType == PsiTypes.booleanType()) {
                    if (!skinValue.isBoolean) {
                        problem("boolean")
                    }
                } else if (expectedType is PsiPrimitiveType) {
                    val check = when (expectedType) {
                        PsiTypes.byteType() -> skinValue.text.toByteOrNull() != null
                        PsiTypes.doubleType() -> skinValue.text.toDoubleOrNull() != null
                        PsiTypes.floatType() -> skinValue.text.toFloatOrNull() != null
                        PsiTypes.intType() -> skinValue.text.toIntOrNull() != null
                        PsiTypes.longType() -> skinValue.text.toLongOrNull() != null
                        PsiTypes.shortType() -> skinValue.text.toShortOrNull() != null
                        else -> true
                    }
                    if (!check) {
                        problem(expectedType.presentableText)
                    }
                } else if (containingClassName == BITMAPFONT_CLASS_NAME
                    && listOf(
                        PROPERTY_NAME_FONT_SCALED_SIZE,
                        PROPERTY_NAME_FONT_MARKUP,
                        PROPERTY_NAME_FONT_FLIP
                    ).contains(propertyName)
                ) {
                    if ((propertyName == PROPERTY_NAME_FONT_MARKUP || propertyName == PROPERTY_NAME_FONT_FLIP) && skinValue.isBoolean) {
                        return
                    } else if (propertyName == PROPERTY_NAME_FONT_SCALED_SIZE && skinValue.text.toIntOrNull() != null) {
                        return
                    }
                    if ((skinValue.reference?.resolve() as? SkinResource)
                            ?.classSpecification
                            ?.getRealClassNamesAsString()
                            ?.contains(expectedType?.canonicalText)
                        == true
                    ) {
                        return
                    }
                    if (propertyName == PROPERTY_NAME_FONT_SCALED_SIZE) {
                        problem("int")
                    } else {
                        problem("boolean")
                    }
                } else if (expectedType is PsiClassType && expectedType.canonicalText != "java.lang.String") {
                    if (skinValue !is SkinStringLiteral && skinValue !is SkinObject) {
                        holder.registerProblem(skinValue, message("skin.inspection.types.resource.expected"))
                    }
                }

            }

        }

}
