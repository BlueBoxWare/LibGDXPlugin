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
package com.gmail.blueboxware.libgdxplugin.inspections.xml

import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectComponent
import com.gmail.blueboxware.libgdxplugin.inspections.utils.testIdMap
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.XmlSuppressableInspectionTool
import com.intellij.psi.PsiElement
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlTag

class XmlTestIdsInspection : XmlSuppressableInspectionTool() {

  override fun getStaticDescription() = message("testid.html.description")

  override fun getID() = "LibGDXTestId"

  override fun getDisplayName() = message("testid.name")

  override fun getGroupPath() = arrayOf("LibGDX", "XML")

  override fun getGroupDisplayName() = "LibGDX"

  override fun isEnabledByDefault() = true

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.WARNING

  override fun isSuppressedFor(element: PsiElement): Boolean {
    return !(element.project.getComponent(LibGDXProjectComponent::class.java)?.isLibGDXProject ?: false) || super.isSuppressedFor(element)
  }

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: XmlElementVisitor() {

    override fun visitXmlTag(tag: XmlTag?) {
      super.visitXmlTag(tag)

      tag?.value?.trimmedText?.let { content ->

        if (testIdMap.containsKey(content)) {
          holder.registerProblem(tag, message("testid.problem.descriptor") + ": " + testIdMap[content])
        }

      }

    }

  }

}
