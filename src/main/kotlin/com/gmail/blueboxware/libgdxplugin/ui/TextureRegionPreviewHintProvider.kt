package com.gmail.blueboxware.libgdxplugin.ui

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2File
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Region
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.getRealClassNamesAsString
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.codeInsight.preview.PreviewHintProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiLiteralExpression
import com.intellij.util.ui.ImageUtil
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import java.awt.image.BufferedImage
import javax.swing.JComponent

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
class TextureRegionPreviewHintProvider : PreviewHintProvider {

    override fun isSupportedFile(file: PsiFile?): Boolean =
        file is SkinFile || file is PsiJavaFile || file is KtFile || file is Atlas2File

    override fun getPreviewComponent(el: PsiElement): JComponent? {

        var element = el

        if (element.containingFile is Atlas2File) {

            element.getParentOfType<Atlas2Region>()?.let { atlasRegion ->
                atlasRegion.image?.let { image ->
                    return createPreviewComponent(image, atlasRegion.name)
                }
            }

        } else {

            if (element.containingFile is SkinFile && element.parent is SkinStringLiteral && element.parent.parent is SkinResourceName) {
                element.getParentOfType<SkinResource>()?.let { resource ->
                    if (resource.classSpecification?.classNameAsString?.plainName == TINTED_DRAWABLE_CLASS_NAME) {
                        element =
                            (resource.`object`?.getProperty(PROPERTY_NAME_TINTED_DRAWABLE_NAME)?.value as? SkinStringLiteral)
                                ?: element
                    }
                }
            }

            when (element.containingFile) {
                is SkinFile -> element.getParentOfType<SkinStringLiteral>(false)
                is PsiJavaFile -> element.getParentOfType<PsiLiteralExpression>()
                is KtFile -> element.getParentOfType<KtStringTemplateExpression>()
                else -> null
            }?.references?.forEach { reference ->
                reference.resolve()?.let { target ->
                    @Suppress("ControlFlowWithEmptyBody")
                    if (target is Atlas2Region) {
                        target.image?.let { image ->
                            return createPreviewComponent(image, target.name)
                        }
                    } else if (
                        target is SkinResource
                        && target
                            .classSpecification
                            ?.getRealClassNamesAsString()
                            ?.contains(TINTED_DRAWABLE_CLASS_NAME) == true
                    ) {

                        val tintedDrawableName = target.name

                        var colorElement = target.`object`?.getProperty(PROPERTY_NAME_TINTED_DRAWABLE_COLOR)?.value

                        while (colorElement is SkinStringLiteral) {
                            colorElement = (colorElement.reference?.resolve() as? SkinResource)?.value
                        }

                        val color = (colorElement as? SkinObject)?.asColor(true)

                        var nameTarget: PsiElement? = target

                        while (
                            (nameTarget as? SkinResource)
                                ?.classSpecification
                                ?.getRealClassNamesAsString()
                                ?.contains(TINTED_DRAWABLE_CLASS_NAME) == true
                        ) {
                            nameTarget =
                                (nameTarget as? SkinResource)
                                    ?.`object`
                                    ?.getProperty(PROPERTY_NAME_TINTED_DRAWABLE_NAME)
                                    ?.value
                                    ?.reference
                                    ?.resolve()
                        }

                        (nameTarget as? Atlas2Region)?.let { atlasRegion ->
                            atlasRegion.image?.let { image ->
                                return createPreviewComponent(color?.let { image.tint(color) } ?: image,
                                    tintedDrawableName)
                            }
                        }

                    } else {

                    }
                }
            }

        }

        return null

    }

    private fun createPreviewComponent(image: BufferedImage, name: String?): JComponent {


        val scale = when {
            (image.width < 10 || image.height < 10) && image.width < 100 && image.height < 100 -> {
                8
            }

            (image.width < 20 || image.height < 20) && image.width < 100 && image.height < 100 -> {
                4
            }

            (image.width < 50 || image.height < 50) && image.width < 200 && image.height < 200 -> {
                2
            }

            else -> {
                1
            }
        }

        val previewImage = ImageUtil.toBufferedImage(ImageUtil.scaleImage(image, scale.toDouble()))

        val txt = (name ?: "<unknown>") + " (" + image.width + " x " + image.height +
                (if (scale != 1) ", shown at scale ${scale}x" else "") + ")"

        return ImagePreviewComponent(previewImage, txt)

    }

}
