package com.gmail.blueboxware.libgdxplugin.filetypes.json.annotators

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonJobject
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonString
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXPluginSettings
import com.gmail.blueboxware.libgdxplugin.utils.COLOR_REGEX
import com.gmail.blueboxware.libgdxplugin.utils.JSON_COLOR_PROPERTY_NAMES
import com.gmail.blueboxware.libgdxplugin.utils.color
import com.gmail.blueboxware.libgdxplugin.utils.createAnnotation
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement


/*
 * Copyright 2019 Blue Box Ware
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
class GdxJsonColorAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        if (!element.project.getService(
                LibGDXPluginSettings::class.java
            ).enableColorAnnotationsInJson
        ) {
            return
        }

        if (element is GdxJsonString) {
            if (element.getValue().firstOrNull() == '#' && COLOR_REGEX.matches(element.getValue())) {
                color(element.getValue())?.let { color ->

                    createAnnotation(color, element, holder)

                }
            }
        } else if (element is GdxJsonJobject) {

            val r = element.getProperty("r") ?: return
            val g = element.getProperty("g") ?: return
            val b = element.getProperty("b") ?: return
            val a = element.getProperty("a")

            val nrOfProperties = element.propertyList.size

            if (!(nrOfProperties == 3 || (nrOfProperties == 4 && a != null))) {
                return
            }

            val rgbR = (r.value?.value as? GdxJsonLiteral)?.toFloatOrNull() ?: return
            val rgbG = (g.value?.value as? GdxJsonLiteral)?.toFloatOrNull() ?: return
            val rgbB = (b.value?.value as? GdxJsonLiteral)?.toFloatOrNull() ?: return
            val rgbA = (a?.value?.value as? GdxJsonLiteral)?.toFloatOrNull() ?: 1.0f

            color(rgbR, rgbG, rgbB, rgbA)?.let { color ->

                createAnnotation(color, element, holder)

            }

        } else if (element is GdxJsonProperty) {

            if (element.propertyName.getValue().toLowerCase() in JSON_COLOR_PROPERTY_NAMES) {

                (element.value?.value as? GdxJsonString)?.getValue()?.let { str ->
                    if (str.length == 6 || str.length == 8) {
                        color(str)?.let {
                            createAnnotation(it, element, holder)
                        }
                    }
                }

            }

        }

    }

}
