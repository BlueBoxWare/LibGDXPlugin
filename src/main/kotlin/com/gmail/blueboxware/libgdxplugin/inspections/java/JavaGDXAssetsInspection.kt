package com.gmail.blueboxware.libgdxplugin.inspections.java

import com.gmail.blueboxware.libgdxplugin.inspections.checkFilename
import com.gmail.blueboxware.libgdxplugin.inspections.checkSkinFilename
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*

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
internal class JavaGDXAssetsInspection : LibGDXJavaBaseInspection() {

    override fun getStaticDescription() = message("gdxassets.annotation.inspection.descriptor")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : JavaElementVisitor() {

            override fun visitAnnotation(annotation: PsiAnnotation?) {

                if (annotation == null || annotation.qualifiedName != ASSET_ANNOTATION_NAME) {
                    return
                }

                if ((annotation.owner as? PsiModifierList)?.context is PsiVariable) {
                    if (annotation.getClassNamesOfOwningVariable().none { it in TARGETS_FOR_GDXANNOTATION }) {
                        holder.registerProblem(
                            annotation,
                            message("gdxassets.annotation.problem.descriptor.wrong.target"),
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                        )
                    }
                }

            }

            override fun visitAnnotationParameterList(list: PsiAnnotationParameterList?) {
                (list?.context as? PsiAnnotation)?.let { annotation ->

                    if (annotation.qualifiedName != ASSET_ANNOTATION_NAME) {
                        return
                    }

                    (annotation.findAttributeValue(ASSET_ANNOTATION_SKIN_PARAM_NAME) as? PsiArrayInitializerMemberValue)
                        ?.initializers
                        ?.forEach {
                            ((it as? PsiLiteralExpression)?.value as? String)?.let { value ->
                                checkSkinFilename(it, value, holder)
                            }
                        }

                    (annotation.findAttributeValue(
                        ASSET_ANNOTATION_SKIN_PARAM_NAME
                    ) as? PsiLiteralExpression)?.let { literal ->
                        (literal.value as? String)?.let { value ->
                            checkSkinFilename(literal, value, holder)
                        }
                    }

                    (annotation.findAttributeValue(
                        ASSET_ANNOTATION_ATLAS_PARAM_NAME
                    ) as? PsiArrayInitializerMemberValue)?.initializers?.forEach {
                        ((it as? PsiLiteralExpression)?.value as? String)?.let { value ->
                            checkFilename(it, value, holder)
                        }
                    }

                    (annotation.findAttributeValue(
                        ASSET_ANNOTATION_ATLAS_PARAM_NAME
                    ) as? PsiLiteralExpression)?.let { literal ->
                        (literal.value as? String)?.let { value ->
                            checkFilename(literal, value, holder)
                        }
                    }

                    (annotation.findAttributeValue(
                        ASSET_ANNOTATION_PROPERTIES_PARAM_NAME
                    ) as? PsiArrayInitializerMemberValue)?.initializers?.forEach {
                        ((it as? PsiLiteralExpression)?.value as? String)?.let { value ->
                            checkFilename(it, value, holder)
                        }
                    }

                    (annotation.findAttributeValue(
                        ASSET_ANNOTATION_PROPERTIES_PARAM_NAME
                    ) as? PsiLiteralExpression)?.let { literal ->
                        (literal.value as? String)?.let { value ->
                            checkFilename(literal, value, holder)
                        }
                    }

                    annotation.parameterList.attributes.forEach { psiNameValuePair ->
                        if (psiNameValuePair.name == ASSET_ANNOTATION_SKIN_PARAM_NAME) {
                            if (annotation.getClassNamesOfOwningVariable().none { it == SKIN_CLASS_NAME }) {
                                registerUselessParameterProblem(
                                    holder, psiNameValuePair, ASSET_ANNOTATION_SKIN_PARAM_NAME, SKIN_CLASS_NAME
                                )
                            }
                        } else if (psiNameValuePair.name == ASSET_ANNOTATION_ATLAS_PARAM_NAME) {
                            if (annotation.getClassNamesOfOwningVariable()
                                    .none { it == TEXTURE_ATLAS_CLASS_NAME || it == SKIN_CLASS_NAME }
                            ) {
                                registerUselessParameterProblem(
                                    holder,
                                    psiNameValuePair,
                                    ASSET_ANNOTATION_ATLAS_PARAM_NAME,
                                    "$SKIN_CLASS_NAME or $TEXTURE_ATLAS_CLASS_NAME"
                                )
                            }
                        } else if (psiNameValuePair.name == ASSET_ANNOTATION_PROPERTIES_PARAM_NAME) {
                            if (annotation.getClassNamesOfOwningVariable().none { it == I18NBUNDLE_CLASS_NAME }) {
                                registerUselessParameterProblem(
                                    holder,
                                    psiNameValuePair,
                                    ASSET_ANNOTATION_PROPERTIES_PARAM_NAME,
                                    I18NBUNDLE_CLASS_NAME
                                )
                            }
                        }
                    }

                }

            }

        }

    private fun PsiAnnotation.getClassNamesOfOwningVariable(): List<String> {

        ((owner as? PsiModifierList)?.context as? PsiVariable)?.type?.let { type ->
            if (type is PsiClassType) {
                return type.resolve()?.supersAndThis()?.mapNotNull { it.qualifiedName } ?: listOf()
            }
        }

        return listOf()

    }

    private fun registerUselessParameterProblem(
        holder: ProblemsHolder,
        psiNameValuePair: PsiNameValuePair,
        parameterName: String,
        className: String
    ) {

        psiNameValuePair.nameIdentifier?.let { identifier ->
            holder.registerProblem(
                identifier,
                message(
                    "gdxassets.annotation.problem.descriptor.useless.parameter",
                    parameterName,
                    className
                )
            )
        }

    }

}
