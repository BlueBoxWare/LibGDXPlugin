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
package com.gmail.blueboxware.libgdxplugin.inspections.gradle

import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectComponent
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.GDXLibrary
import com.gmail.blueboxware.libgdxplugin.utils.GradleBuildFileVersionsVisitor
import com.gmail.blueboxware.libgdxplugin.utils.compareVersionStrings
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.lang.psi.GroovyElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.GroovyFileBase
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementVisitor

class GradleOutdatedVersionsInspection : LibGDXGradleBaseInspection() {

  companion object {

    fun isOutdatedVersion(foundVersion: String, project: Project, library: GDXLibrary): Boolean {

        project.getComponent(LibGDXProjectComponent::class.java)?.let { projectComponent ->
          val latestVersion = projectComponent.getLatestLibraryVersion(library)
          try {
            if (latestVersion != null && compareVersionStrings(latestVersion, foundVersion) > 0) {
              return true
            }
          } catch (e: IllegalArgumentException) {
            // just ignore
          }
        }

      return false

    }

  }

  override fun getStaticDescription() = message("outdated.versions.html.description")

  override fun getID() = "LibGDXOutdatedVersion"

  override fun getDisplayName() = message("outdated.versions.inspection.name")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = GroovyPsiElementVisitor(object : GroovyElementVisitor() {

    override fun visitFile(file: GroovyFileBase) {

      if (!FileUtilRt.extensionEquals(file.name, "gradle")) return

      file.accept(GroovyPsiElementVisitor(object: GradleBuildFileVersionsVisitor() {

        override fun onVersionFound(library: GDXLibrary, version: String, element: PsiElement) {
          if (isOutdatedVersion(version, element.project, library)) {
            holder.registerProblem(element, message("outdated.versions.problem.descriptor"))
          }
        }
      }))
    }
  })
}
