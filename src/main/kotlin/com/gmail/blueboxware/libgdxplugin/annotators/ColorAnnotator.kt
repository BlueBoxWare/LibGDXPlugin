package com.gmail.blueboxware.libgdxplugin.annotators

import com.gmail.blueboxware.libgdxplugin.settings.LibGDXPluginSettings
import com.gmail.blueboxware.libgdxplugin.utils.GutterColorRenderer
import com.gmail.blueboxware.libgdxplugin.utils.getColor
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDXProject
import com.gmail.blueboxware.libgdxplugin.utils.key
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.refactoring.getLineNumber
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
class ColorAnnotator: Annotator {

  private val ANNOTATIONS_KEY = key<MutableList<Pair<Int, Color>>>("annotations")

  override fun annotate(element: PsiElement, holder: AnnotationHolder) {

    if (element.project.isLibGDXProject() && (ServiceManager.getService(element.project, LibGDXPluginSettings::class.java)?.enableColorAnnotations == true)) {

      element.getColor()?.let { color ->
        annotateWithColor(color, element, holder)
      }
    }

  }


  private fun annotateWithColor(color: Color, element: PsiElement, holder: AnnotationHolder) {

    val annotationSessions = holder.currentAnnotationSession

    if (annotationSessions.getUserData(ANNOTATIONS_KEY) == null) {
      annotationSessions.putUserData(ANNOTATIONS_KEY, mutableListOf())
    }

    annotationSessions.getUserData(ANNOTATIONS_KEY)?.let { currentAnnotations ->
      for ((first, second) in currentAnnotations) {
        if (first == element.getLineNumber() && second == color) {
          return
        }
      }

      currentAnnotations.add(element.getLineNumber() to color)
    }

    if (ApplicationManager.getApplication()?.isUnitTestMode == true) {
      val msg = String.format("#%02x%02x%02x%02x", color.red, color.green, color.blue, color.alpha)
      holder.createWeakWarningAnnotation(element, msg)
    } else {
      val annotation = holder.createInfoAnnotation(element, null)
      annotation.gutterIconRenderer = GutterColorRenderer(color)
    }

  }

}