package com.gmail.blueboxware.libgdxplugin.filetypes.skin.annotators

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassSpecification
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.utils.GutterColorRenderer
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilBase
import com.intellij.ui.ColorChooser
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

    if (element is SkinObject) {

      val force = (PsiTreeUtil.findFirstParent(element, { it is SkinClassSpecification }) as? SkinClassSpecification)?.classNameAsString == "com.badlogic.gdx.graphics.Color"
        || element.resolveToTypeString() == "com.badlogic.gdx.graphics.Color"

      element.asColor(force)?.let { color ->

        val annotation = createAnnotation(color, element, holder, createIcon = false)
        annotation.gutterIconRenderer = object : GutterColorRenderer(color) {
          override fun getClickAction() = object : AnAction() {
            override fun actionPerformed(e: AnActionEvent?) {
              if (!element.isWritable) return

              val editor = PsiUtilBase.findEditor(element) ?: return

              val newColor = ColorChooser.chooseColor(editor.component, "Choose Color", color, true, true)

              if (newColor != null) {
                ApplicationManager.getApplication().runWriteAction {
                  element.setColor(newColor)?.let {
                    element.replace(it)
                  }
                }
              }
            }
          }
        }
      }

    } else if (element is SkinStringLiteral) {

      (element.context as? SkinResource)?.let { resource ->

        resource.asColor(false)?.let { color ->
          createAnnotation(color, element, holder)
        }

        return

      }

      if (element.resolveToTypeString() == "com.badlogic.gdx.graphics.Color") {

        (element.reference?.resolve() as? SkinResource)?.asColor(false)?.let { color ->
          createAnnotation(color, element, holder)
        }

      }
    }

  }

  private fun createAnnotation(color: Color, element: PsiElement, holder: AnnotationHolder, createIcon: Boolean = true) =
          if (ApplicationManager.getApplication().isUnitTestMode) {
            holder.createWeakWarningAnnotation(element, String.format("#%02x%02x%02x%02x", color.red, color.green, color.blue, color.alpha))
          } else {
            holder.createInfoAnnotation(element, null).apply {
              if (createIcon) {
                gutterIconRenderer = GutterColorRenderer(color)
              }
            }
          }

}