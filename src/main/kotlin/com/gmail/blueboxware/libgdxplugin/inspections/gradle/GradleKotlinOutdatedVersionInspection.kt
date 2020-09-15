package com.gmail.blueboxware.libgdxplugin.inspections.gradle

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtValueArgumentList
import org.jetbrains.kotlin.psi.KtVisitorVoid


/*
 * Copyright 2018 Blue Box Ware
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
class GradleKotlinOutdatedVersionInspection: AbstractKotlinInspection() {

  override fun getGroupPath() = arrayOf("libGDX", "Gradle")

  override fun getGroupDisplayName() = "libGDX"

  override fun getID() = "LibGDXOutdatedVersionGradleKotlin"

  override fun isEnabledByDefault() = true

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.WARNING

  override fun getStaticDescription() = message("outdated.version.inspection.static.description", Libraries.listOfCheckedLibraries())

  override fun getDisplayName() = message("outdated.version.inspection.display.name.gradle.kotlin")

  override val suppressionKey = id

  override fun isSuppressedFor(element: PsiElement): Boolean {
    return !element.project.isLibGDXProject() || super.isSuppressedFor(element)
  }

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: KtVisitorVoid() {

    override fun visitValueArgumentList(list: KtValueArgumentList) {

      if (!list.isInGradleKotlinBuildFile()) {
        return
      }

      getLibraryInfoFromKotlinArgumentList(list)?.let { (lib, version) ->
        checkVersionAndReport(holder, list, lib, version)
      }

    }

    override fun visitStringTemplateExpression(expression: KtStringTemplateExpression) {

      if (!expression.isInGradleKotlinBuildFile()) {
        return
      }

      getLibraryInfoFromKotlinString(expression)?.let { (lib, version) ->
        checkVersionAndReport(holder, expression, lib, version)
      }

    }

  }

}