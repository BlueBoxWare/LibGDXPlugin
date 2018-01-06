package com.gmail.blueboxware.libgdxplugin.inspections.gradle

import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.lang.properties.psi.PropertiesFile
import com.intellij.lang.properties.psi.PropertiesList
import com.intellij.lang.properties.psi.Property
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil

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

// TODO:
// Use PropertiesInspectionBase instead of LocalInspectionTool and remove isSuppressedFor()
// once AS gets to version 163
//
abstract class LibGDXGradlePropertiesBaseInspection : LocalInspectionTool() {

  override fun getGroupPath() = arrayOf("LibGDX", "Gradle")

  override fun getGroupDisplayName() = "LibGDX"

  override fun isEnabledByDefault() = true

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.WARNING

  //
  // https://github.com/JetBrains/intellij-community/commit/2067f07637bdb4f361b4b458d10f943cad1115d4#diff-14bfde8b041139ed23acddb76424276a
  //
  override fun isSuppressedFor(element: PsiElement): Boolean {

    val property = PsiTreeUtil.getParentOfType(element, Property::class.java, false)
    val file: PropertiesFile?

    if (property == null) {
      file = element.containingFile as? PropertiesFile ?: return false
    } else {
      var prev = property.prevSibling
      while (prev is PsiWhiteSpace || prev is PsiComment) {
        if (prev is PsiComment) {
          val text = prev.text
          if (text.contains("suppress") && text.contains("\"$id\"")) {
            return true
          }
        }
        prev = prev.prevSibling
      }
      file = property.propertiesFile
    }

    var leaf = file?.containingFile?.findElementAt(0) ?: return false

    while (leaf is PsiWhiteSpace) {
      leaf = leaf.nextSibling
    }

    while (leaf is PsiComment) {
      val text = leaf.text
      if (text.contains("suppress") && text.contains("\"$id\"") && text.contains("file")) {
        return true
      }
      leaf = leaf.nextSibling
      if (leaf is PsiWhiteSpace) {
        leaf = leaf.nextSibling
      }
      if (leaf is PropertiesList && leaf.firstChild == property && text.contains("suppress") && text.contains("\"$id\"")) {
        return true
      }
    }

    return false
  }

}