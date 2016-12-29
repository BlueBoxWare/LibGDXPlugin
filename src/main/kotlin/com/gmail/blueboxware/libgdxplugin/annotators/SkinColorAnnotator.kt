package com.gmail.blueboxware.libgdxplugin.annotators

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinNumberLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.utils.GutterColorRenderer
import com.gmail.blueboxware.libgdxplugin.utils.stringToColor
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import java.awt.Color

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
class SkinColorAnnotator : Annotator {

  override fun annotate(element: PsiElement, holder: AnnotationHolder) {

    if (element is SkinProperty) {

      if (element.name.toLowerCase() == "hex") {

        (element.propertyValue.value as? SkinStringLiteral)?.value?.let { string ->

          stringToColor(string)?.let { color ->
            val annotation = holder.createInfoAnnotation(element, null)
            annotation.gutterIconRenderer = GutterColorRenderer(color)
          }

        }

      } else  {

        (element.value as? SkinObject)?.let { obj ->

          var r: Float? = null
          var g: Float? = null
          var b: Float? = null
          var a: Float = 1.0f

          for (property in obj.propertyList) {

            (property.value as? SkinNumberLiteral)?.value?.toFloat()?.let { d ->

              when (property.name) {
                "r" -> r = d
                "g" -> g = d
                "b" -> b = d
                "a" -> a = d
              }

            }
          }

          if (r != null && g != null && b != null) {

            try {
              val color = Color(r ?: 0f, g ?: 0f, b ?: 0f, a)
              val annotation = holder.createInfoAnnotation(element, null)
              annotation.gutterIconRenderer = GutterColorRenderer(color)
            } catch (e: IllegalArgumentException) {
              // Do nothing
            }

          }
        }

      }

    }

  }

}