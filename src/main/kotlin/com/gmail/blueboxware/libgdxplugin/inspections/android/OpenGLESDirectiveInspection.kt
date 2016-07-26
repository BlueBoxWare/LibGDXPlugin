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
package com.gmail.blueboxware.libgdxplugin.inspections.android

import com.gmail.blueboxware.libgdxplugin.inspections.utils.isLibGDXProject
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.XmlSuppressableInspectionTool
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlDocument
import com.intellij.psi.xml.XmlTag

class OpenGLESDirectiveInspection : XmlSuppressableInspectionTool() {

  override fun getStaticDescription() = message("no.opengl.html.description")

  override fun getID() = "LibGDXOpenGLVersion"

  override fun getDisplayName() = message("no.opengl.directive.display.name")

  override fun getGroupPath() = arrayOf("LibGDX", "Android")

  override fun getGroupDisplayName() = "LibGDX"

  override fun isEnabledByDefault() = true

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.WARNING

  override fun isSuppressedFor(element: PsiElement): Boolean {
    return !isLibGDXProject(element.project) || super.isSuppressedFor(element)
  }

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {

    if (holder.file.name != "AndroidManifest.xml") {
      return super.buildVisitor(holder, isOnTheFly)
    }

    return object : XmlElementVisitor() {

      private var openGLESDirectiveFound = false
      private var problemElement: XmlTag? = null

      override fun visitXmlDocument(document: XmlDocument?) {
        super.visitXmlDocument(document)

        if (!openGLESDirectiveFound) {
          val element = problemElement ?: document?.rootTag
          element?.let { element ->
            if (element.text != "") {
              holder.registerProblem(element, message("no.opengl.directive.problem.descriptor"))
            }
          }
        }
      }

      override fun visitXmlTag(tag: XmlTag?) {
        super.visitXmlTag(tag)

        if (tag == null) return

        val tagName = tag.name.toLowerCase()

        if (tagName == "manifest" && problemElement == null) {
          problemElement = tag
        }

        if (tagName == "uses-feature" && tag.parentTag?.name?.toLowerCase() == "manifest") {

          var directiveFound = false
          var isRequired = true

          for (attribute in tag.attributes) {
            if (attribute.name == "android:glEsVersion") {
              if (attribute?.value == "0x00020000" || attribute?.value == "0x00030000") {
                directiveFound = true
              }
              problemElement = tag
            } else if (attribute.name == "android:required" && attribute.value == "false") {
              isRequired = false
            }
          }

          if (directiveFound && isRequired) {
            openGLESDirectiveFound = true
          }

        }
      }

    }
  }

}
