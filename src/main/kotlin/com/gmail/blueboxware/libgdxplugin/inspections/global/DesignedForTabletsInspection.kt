package com.gmail.blueboxware.libgdxplugin.inspections.global

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.androidManifest.ManifestModel
import com.gmail.blueboxware.libgdxplugin.utils.androidManifest.SdkVersionType
import com.gmail.blueboxware.libgdxplugin.utils.firstParent
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDXProject
import com.intellij.analysis.AnalysisScope
import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import org.jetbrains.kotlin.idea.search.projectScope
import org.jetbrains.plugins.groovy.lang.psi.GroovyFileBase
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.GroovyRecursiveElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrApplicationStatement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.path.GrCallExpression

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
class DesignedForTabletsInspection: GlobalInspectionTool() {

  override fun getDisplayName() = message("designed.for.tablets.inspection")

  @Suppress("DialogTitleCapitalization")
  override fun getGroupDisplayName() = "libGDX"

  override fun getStaticDescription() = message("designed.for.tablets.html.description")

  override fun isEnabledByDefault() = true

  override fun getShortName() = "LibGDXDesignedForTablets"

  override fun runInspection(
          scope: AnalysisScope,
          manager: InspectionManager,
          globalContext: GlobalInspectionContext,
          problemDescriptionsProcessor: ProblemDescriptionsProcessor
  ) {

    if (!globalContext.project.isLibGDXProject()) {
      return
    }

    val problems = mutableListOf<Pair<PsiElement, String>>()
    val versionsMap = mutableMapOf<SdkVersionType, Int>()

    val gradleFiles =
            FilenameIndex.getFilesByName(globalContext.project, "build.gradle", globalContext.project.projectScope())

    gradleFiles.sortBy { it.virtualFile.path }

    for (gradleFile in gradleFiles) {
      gradleFile.accept(GroovyPsiElementVisitor(DesignedForTabletsGradleVisitor(problems, versionsMap)))
    }

    val manifests =
            FilenameIndex.getFilesByName(
                    globalContext.project,
                    "AndroidManifest.xml",
                    globalContext.project.projectScope()
            )

    for (manifest in manifests) {
      (manifest as? XmlFile)?.let {
        processManifest(problems, it, versionsMap)
      }
    }

    for ((element, msg) in problems) {
      if (!isSuppressedFor(element)) {
        problemDescriptionsProcessor.addProblemElement(
                globalContext.refManager.getReference(element.containingFile),
                manager.createProblemDescriptor(
                        element,
                        msg,
                        false,
                        null,
                        ProblemHighlightType.WEAK_WARNING
                )
        )
      }
    }

  }

  private fun processManifest(
          problems: MutableList<Pair<PsiElement, String>>,
          manifest: XmlFile,
          versionsMap: Map<SdkVersionType, Int>
  ) {

    val model = ManifestModel.fromFile(manifest)
    model.applyExternalVersions(versionsMap)

    val versionTag = (model.targetSDK?.element ?: model.minSDK.element ?: model.maxSDK?.element)?.let { attribute ->
      attribute.firstParent { it is XmlTag }
    } ?: manifest
    if (model.resolveTargetSDK() < 11 && model.minSDK.value < 11) {
      problems.add(versionTag to message("designed.for.tablets.problem.descriptor.target.or.min"))
    } else if (model.maxSDK?.value ?: 11 < 11) {
      problems.add(versionTag to message("designed.for.tablets.problem.descriptor.max"))
    }

    if (model.supportScreens == null && model.minSDK.value < 13) {
      problems.add(manifest to message("designed.for.tablets.problem.descriptor.missing.support.screens"))
    } else {
      val supportScreens = model.resolveSupportsScreens()
      val supportScreensElement = model.supportScreens?.element ?: manifest

      if (
              (model.hasLargeScreensSupportAttribute && supportScreens.largeScreens != true)
              || (model.hasXLargeScreenSupportAttribute && supportScreens.xlargeScreens != true)
      ) {
        problems.add(supportScreensElement to message("designed.for.tablets.problem.descriptor.large.false"))
      }

      if (model.minSDK.value < 13 && (!model.hasLargeScreensSupportAttribute || !model.hasXLargeScreenSupportAttribute)) {
        problems.add(supportScreensElement to message("designed.for.tablets.problem.descriptor.large.missing"))
      }
    }

  }

}

private class DesignedForTabletsGradleVisitor(
        val problems: MutableList<Pair<PsiElement, String>>,
        val versionsMap: MutableMap<SdkVersionType, Int>
): GroovyRecursiveElementVisitor() {

  private var foundElementMap: MutableMap<SdkVersionType, GrMethodCall> = mutableMapOf()

  private fun updateVersionMap(call: GrMethodCall) {

    val invokedText = call.invokedExpression.text

    if (invokedText != "minSdkVersion" && invokedText != "maxSdkVersion" && invokedText != "targetSdkVersion") return

    if (call.argumentList.allArguments.isEmpty()) return

    val argument = call.argumentList.allArguments[0]

    val version = ((argument as? GrLiteral)?.value as? Int) ?: return

    var type: SdkVersionType? = null

    when (invokedText) {
      "maxSdkVersion" -> type = SdkVersionType.MAX
      "minSdkVersion" -> type = SdkVersionType.MIN
      "targetSdkVersion" -> type = SdkVersionType.TARGET
    }

    type?.let { typeNotNull ->
      if (version > versionsMap[typeNotNull] ?: 0) {
        versionsMap[typeNotNull] = version
      }
      foundElementMap[typeNotNull] = call
    }

    if (invokedText == "maxSdkVersion" && versionsMap[SdkVersionType.MAX] ?: 11 < 11) {
      if (problems.none { it.first == call }) {
        problems.add(call to message("designed.for.tablets.problem.descriptor.max"))
      }
    }

  }

  override fun visitFile(file: GroovyFileBase) {
    super.visitFile(file)

    if (foundElementMap[SdkVersionType.TARGET] != null || foundElementMap[SdkVersionType.MIN] != null) {
      if (versionsMap[SdkVersionType.TARGET] ?: 11 < 11 && versionsMap[SdkVersionType.MIN] ?: 11 < 11) {
        foundElementMap[SdkVersionType.TARGET]?.let {
          problems.add(
                  it to message("designed.for.tablets.problem.descriptor.target.or.min")
          )
        }
        foundElementMap[SdkVersionType.MIN]?.let {
          problems.add(
                  it to message("designed.for.tablets.problem.descriptor.target.or.min")
          )
        }
      }
    }
  }

  override fun visitCallExpression(callExpression: GrCallExpression) {
    super.visitCallExpression(callExpression)

    if (callExpression is GrMethodCall) {
      updateVersionMap(callExpression)
    }

  }

  override fun visitApplicationStatement(applicationStatement: GrApplicationStatement) {
    super.visitApplicationStatement(applicationStatement)

    updateVersionMap(applicationStatement)

  }
}