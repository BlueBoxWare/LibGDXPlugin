package com.gmail.blueboxware.libgdxplugin.inspections.kotlin

import com.gmail.blueboxware.libgdxplugin.inspections.checkFilename
import com.gmail.blueboxware.libgdxplugin.inspections.checkSkinFilename
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.impl.VariableDescriptorImpl
import org.jetbrains.kotlin.idea.search.usagesSearch.descriptor
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

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
internal class KotlinGDXAssetsInspection : LibGDXKotlinBaseInspection() {

    override fun getStaticDescription() = message("gdxassets.annotation.inspection.descriptor")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object : KtVisitorVoid() {

        override fun visitAnnotationEntry(annotationEntry: KtAnnotationEntry) {

            annotationEntry.analyzePartial().get(BindingContext.ANNOTATION, annotationEntry)?.type?.fqName()
                ?.let { fqName ->

                    if (fqName != ASSET_ANNOTATION_NAME) return

                    val classNamesOfOwningVariable = annotationEntry.getClassNamesOfOwningVariable()

                    annotationEntry.valueArgumentList?.arguments?.forEach { ktValueArgument ->

                        val name = ktValueArgument.getArgumentName()?.asName?.identifier

                        val arguments = ktValueArgument.getArgumentExpression().let { argumentExpression ->
                            if (argumentExpression is KtCallExpression) {
                                argumentExpression.valueArgumentList?.arguments?.map { it.getArgumentExpression() }
                            } else {
                                (argumentExpression as? KtCollectionLiteralExpression)?.innerExpressions
                            }
                        }

                        if (name == ASSET_ANNOTATION_SKIN_PARAM_NAME) {
                            if (SKIN_CLASS_NAME !in classNamesOfOwningVariable) {
                                registerUselessParameterProblem(
                                    holder,
                                    ktValueArgument,
                                    ASSET_ANNOTATION_SKIN_PARAM_NAME,
                                    SKIN_CLASS_NAME
                                )
                            }
                        } else if (name == ASSET_ANNOTATION_ATLAS_PARAM_NAME) {
                            if (SKIN_CLASS_NAME !in classNamesOfOwningVariable && TEXTURE_ATLAS_CLASS_NAME !in classNamesOfOwningVariable) {
                                registerUselessParameterProblem(
                                    holder,
                                    ktValueArgument,
                                    ASSET_ANNOTATION_ATLAS_PARAM_NAME,
                                    "$SKIN_CLASS_NAME or $TEXTURE_ATLAS_CLASS_NAME"
                                )
                            }
                        } else if (name == ASSET_ANNOTATION_PROPERTIES_PARAM_NAME) {
                            if (I18NBUNDLE_CLASS_NAME !in classNamesOfOwningVariable) {
                                registerUselessParameterProblem(
                                    holder,
                                    ktValueArgument,
                                    ASSET_ANNOTATION_PROPERTIES_PARAM_NAME,
                                    I18NBUNDLE_CLASS_NAME
                                )
                            }
                        }

                        arguments?.forEach { argument ->
                            (argument as? KtStringTemplateExpression)?.asPlainString()?.let { value ->

                                when (name) {
                                    ASSET_ANNOTATION_SKIN_PARAM_NAME -> checkSkinFilename(argument, value, holder)
                                    ASSET_ANNOTATION_ATLAS_PARAM_NAME -> checkFilename(argument, value, holder)
                                    ASSET_ANNOTATION_PROPERTIES_PARAM_NAME -> checkFilename(argument, value, holder)
                                }

                                Unit
                            }
                        }

                    }

                    if ((annotationEntry.context as? KtModifierList)?.owner is KtVariableDeclaration) {
                        if (classNamesOfOwningVariable.none { it in TARGETS_FOR_GDXANNOTATION }) {
                            holder.registerProblem(
                                annotationEntry,
                                message("gdxassets.annotation.problem.descriptor.wrong.target"),
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                            )
                        }
                    }

                }

        }
    }

    private fun registerUselessParameterProblem(
        holder: ProblemsHolder,
        ktValueArgument: KtValueArgument,
        parameterName: String,
        className: String
    ) {

        ktValueArgument.getArgumentName()?.let { argumentName ->
            holder.registerProblem(
                argumentName,
                message(
                    "gdxassets.annotation.problem.descriptor.useless.parameter",
                    parameterName,
                    className
                )
            )
        }

    }

}

private fun KtAnnotationEntry.getClassNamesOfOwningVariable(): List<String> {

    ((context as? KtModifierList)?.owner as? KtVariableDeclaration)?.let { ktVariableDeclaration ->

        (ktVariableDeclaration.descriptor as? VariableDescriptorImpl)?.type?.let { type ->
            (type.constructor.declarationDescriptor as? ClassDescriptor)?.let { classDescriptor ->
                return classDescriptor.supersAndThis().map { it.fqNameSafe.asString() }
            }
        }

    }

    return listOf()

}
