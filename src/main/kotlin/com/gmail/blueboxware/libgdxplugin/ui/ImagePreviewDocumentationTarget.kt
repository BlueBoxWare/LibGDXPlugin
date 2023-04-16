/*
 * Copyright 2023 Blue Box Ware
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

@file:Suppress("UnstableTypeUsedInSignature", "UnstableApiUsage")

package com.gmail.blueboxware.libgdxplugin.ui

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Region
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.getRealClassNamesAsString
import com.gmail.blueboxware.libgdxplugin.utils.PROPERTY_NAME_TINTED_DRAWABLE_COLOR
import com.gmail.blueboxware.libgdxplugin.utils.PROPERTY_NAME_TINTED_DRAWABLE_NAME
import com.gmail.blueboxware.libgdxplugin.utils.TINTED_DRAWABLE_CLASS_NAME
import com.gmail.blueboxware.libgdxplugin.utils.tint
import com.intellij.model.Pointer
import com.intellij.navigation.TargetPresentation
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.psi.PsiElement
import com.intellij.util.ui.ImageUtil
import org.jetbrains.kotlin.psi.psiUtil.createSmartPointer
import java.awt.image.BufferedImage
import java.net.URI
import java.net.URISyntaxException
import javax.imageio.ImageIO

class ImagePreviewDocumentationTarget(private val targetElement: PsiElement?) : DocumentationTarget {

    override fun computeDocumentationHint(): String? {

        (targetElement as? Atlas2Region)?.image?.let { image ->
            return createDoc(image, targetElement.name, null)
        }

        targetElement?.references?.forEach { reference ->
            reference.resolve()?.let { target ->
                @Suppress("ControlFlowWithEmptyBody") if (target is Atlas2Region) {
                    target.image?.let { image ->
                        return createDoc(image, target.name, target.containingFile.name)
                    }
                } else if (target is SkinResource && target.classSpecification?.getRealClassNamesAsString()
                        ?.contains(TINTED_DRAWABLE_CLASS_NAME) == true
                ) {

                    val tintedDrawableName = target.name

                    var colorElement = target.`object`?.getProperty(PROPERTY_NAME_TINTED_DRAWABLE_COLOR)?.value

                    while (colorElement is SkinStringLiteral) {
                        colorElement = (colorElement.reference?.resolve() as? SkinResource)?.value
                    }

                    val color = (colorElement as? SkinObject)?.asColor(true)

                    var nameTarget: PsiElement? = target

                    while ((nameTarget as? SkinResource)?.classSpecification?.getRealClassNamesAsString()
                            ?.contains(TINTED_DRAWABLE_CLASS_NAME) == true
                    ) {
                        nameTarget =
                            (nameTarget as? SkinResource)?.`object`?.getProperty(PROPERTY_NAME_TINTED_DRAWABLE_NAME)?.value?.reference?.resolve()
                    }

                    (nameTarget as? Atlas2Region)?.let { atlasRegion ->
                        atlasRegion.image?.let { image ->
                            return createDoc(color?.let { image.tint(color) } ?: image,
                                tintedDrawableName,
                                target.containingFile.name)
                        }
                    }

                } else {

                }
            }
        }

        return null
    }

    override fun computePresentation(): TargetPresentation = TargetPresentation.builder("Test").presentation()

    override fun createPointer(): Pointer<out DocumentationTarget> {
        val originalElementPtr = targetElement?.createSmartPointer()
        return Pointer {
            ImagePreviewDocumentationTarget(originalElementPtr?.dereference())
        }
    }

    private fun createDoc(image: BufferedImage, name: String?, file: String?): String? {

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
        val imageFile = FileUtil.createTempFile("img", ".png", true)
        try {
            ImageIO.write(previewImage, "png", imageFile)
        } catch (e: Throwable) {
            LOG.error("Could not create image file.", e)
            return null
        }

        try {
            var path: String = imageFile.path
            if (SystemInfo.isWindows) {
                path = "/$path"
            }
            val url = URI("file", null, path, null).toString()
            val img: HtmlChunk.Element = HtmlChunk.tag("img").attr("src", url).attr("width", previewImage.width)
                .attr("height", previewImage.height)

            return HtmlBuilder().append(img).append(HtmlChunk.p().addText(name ?: "<unknown>")).also {
                if (file != null) {
                    it.append(HtmlChunk.p().addText("in: $file"))
                }
            }.append(
                HtmlChunk.p().addText(
                    image.width.toString() + " x " + image.height + (if (scale != 1) ", shown at scale ${scale}x" else "")
                )
            ).toString()
        } catch (e: URISyntaxException) {
            // nothing
            LOG.error("Could not create image preview.", e)
            return null
        }

    }


    companion object {
        val LOG = Logger.getInstance(ImagePreviewDocumentationTarget::class.java)
    }

}
