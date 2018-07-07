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

import com.gmail.blueboxware.libgdxplugin.components.VersionManager
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.isInGradleBuildFile
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.plugins.groovy.lang.psi.GroovyElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElement
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrCommandArgumentList
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

class GradleOutdatedVersionsInspection : LibGDXGradleBaseInspection() {

  override fun getStaticDescription() = message("outdated.version.inspection.static.description", Libraries.listOfCheckedLibraries())

  override fun getID() = "LibGDXOutdatedVersionGradle"

  override fun getDisplayName() = message("outdated.version.inspection.display.name.gradle")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = GroovyPsiElementVisitor(object : GroovyElementVisitor() {

    val versionManager = holder.project.getComponent(VersionManager::class.java)

    override fun visitLiteralExpression(literal: GrLiteral) {
      process(literal)
    }

    override fun visitCommandArguments(argumentList: GrCommandArgumentList) {
      process(argumentList)
    }

    override fun visitAssignmentExpression(expression: GrAssignmentExpression) {
      process(expression)
    }

    private fun process(element: GroovyPsiElement) {

      if (versionManager == null || !element.isInGradleBuildFile()) return

      Libraries.extractLibraryInfoFromGroovyConstruct(element)?.let { (lib, usedVersion) ->
        val latestVersion = versionManager.getLatestVersion(lib) ?: return

        if (usedVersion < latestVersion) {
          holder.registerProblem(
                  element,
                  message("outdated.version.inspection.msg", lib.library.name, latestVersion)
          )
        }

        return
      }

      if (element is GrLiteral) {

        Libraries.fromGroovyLiteral(element)?.let { lib ->
          element.project.getComponent(VersionManager::class.java)?.let { versionManager ->
            val usedVersion = versionManager.getUsedVersion(lib)
            val latestVersion = versionManager.getLatestVersion(lib)
            if (usedVersion != null && latestVersion != null && usedVersion < latestVersion) {
              holder.registerProblem(
                      element,
                      message("outdated.version.inspection.msg", lib.library.name, latestVersion)
              )
            }
          }
        }

      }

    }

  })
}
