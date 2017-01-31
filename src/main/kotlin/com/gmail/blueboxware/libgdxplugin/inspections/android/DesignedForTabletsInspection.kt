package com.gmail.blueboxware.libgdxplugin.inspections.android

import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.analysis.AnalysisScope
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement
import com.intellij.psi.XmlRecursiveElementVisitor
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlDocument
import com.intellij.psi.xml.XmlTag
import org.jetbrains.plugins.groovy.lang.psi.GroovyFileBase
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.GroovyRecursiveElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrApplicationStatement
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.path.GrCallExpression
import java.util.*

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

private enum class SdkVersionType { MIN, MAX, TARGET }

class DesignedForTabletsInspection: GlobalInspectionTool() {

  override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.WARNING

  override fun getDisplayName() = message("designed.for.tablets.inspection")

  override fun getGroupPath() = arrayOf("LibGDX", "Android")

  override fun getGroupDisplayName() = "LibGDX"

  override fun getStaticDescription() = message("designed.for.tablets.html.description")

  override fun isEnabledByDefault() = true

  override fun getShortName() = "LibGDXDesignedForTablets"

  override fun runInspection(scope: AnalysisScope, manager: InspectionManager, globalContext: GlobalInspectionContext, problemDescriptionsProcessor: ProblemDescriptionsProcessor) {

    val problems = mutableListOf<Pair<PsiElement, String>>()
    val versionsMap = mutableMapOf<SdkVersionType, Int>()

    val gradleFiles = FilenameIndex.getFilesByName(globalContext.project, "build.gradle", GlobalSearchScope.projectScope(globalContext.project))

    gradleFiles.sortBy { it.virtualFile.path }

    for (gradleFile in gradleFiles) {
      gradleFile.accept(GroovyPsiElementVisitor(DesignedForTabletsGradleVisitor(problems, versionsMap)))
    }

    val manifests = FilenameIndex.getFilesByName(globalContext.project, "AndroidManifest.xml", GlobalSearchScope.projectScope(globalContext.project))

    for (manifest in manifests) {
      manifest.accept(DesignedForTabletsManifestVisitor(problems, versionsMap))
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
}

private class DesignedForTabletsManifestVisitor(val problems: MutableList<Pair<PsiElement, String>>, versionsMap: Map<SdkVersionType, Int>): XmlRecursiveElementVisitor() {

  val localVersionsMap: HashMap<SdkVersionType, Int> = HashMap(versionsMap)

  var supportsScreensFound = false

  override fun visitXmlDocument(document: XmlDocument?) {
    super.visitXmlDocument(document)

    if (document != null && !supportsScreensFound && localVersionsMap[SdkVersionType.MIN] ?: 1 < 13) {
      problems.add(document to message("designed.for.tablets.problem.descriptor.missing.support.screens"))
    }
  }

  override fun visitXmlTag(tag: XmlTag?) {
    super.visitXmlTag(tag)

    if (tag == null) return

    val tagName = tag.name.toLowerCase()

    if (tagName == "uses-sdk") {
      val versionsMap = getVersionsFromUsesSdkElement(tag)

      if (versionsMap[SdkVersionType.TARGET] ?: 0 < 11 && versionsMap[SdkVersionType.MIN] ?: 0 < 11) {
        problems.add(tag to message("designed.for.tablets.problem.descriptor.target.or.min"))
      } else if (versionsMap[SdkVersionType.MAX] ?: 11 < 11) {
        problems.add(tag to message("designed.for.tablets.problem.descriptor.max"))
      }

      updateVersionMap(localVersionsMap, versionsMap)

    } else if (tagName  == "supports-screens") {

      supportsScreensFound = true

      var largeScreensFound = false
      var xlargeScreensFound = false

      for (attribute in tag.attributes) {
        if (attribute.name == "android:largeScreens" || attribute.name == "android:xlargeScreens") {
          if (attribute.value == "false") {
            problems.add(tag to message("designed.for.tablets.problem.descriptor.large.false"))
            return
          } else if (attribute.value == "true" ){
            if (attribute.name == "android:largeScreens") largeScreensFound = true else xlargeScreensFound = true
          }
        }
      }

      if (localVersionsMap[SdkVersionType.MIN] ?: 13 < 13) {
        if (!largeScreensFound || !xlargeScreensFound) {
          problems.add(tag to message("designed.for.tablets.problem.descriptor.large.missing"))
        }
      }

    }

  }

}

private class DesignedForTabletsGradleVisitor(val problems: MutableList<Pair<PsiElement, String>>, val versionsMap: MutableMap<SdkVersionType, Int>): GroovyRecursiveElementVisitor() {

  private var foundElementMap: MutableMap<SdkVersionType, GrMethodCall> = mutableMapOf()

  private fun updateVersionMap(call: GrMethodCall) {

    val invokedText = call.invokedExpression.text

    if (invokedText != "minSdkVersion" && invokedText != "maxSdkVersion" && invokedText != "targetSdkVersion") return

    if (call.argumentList.allArguments.isEmpty()) return

    val argument = call.argumentList.allArguments[0]

    val version = ((argument as? GrLiteral)?.value as? Int) ?: return

    var type: SdkVersionType? = null

    when (invokedText) {
      "maxSdkVersion" ->  type = SdkVersionType.MAX
      "minSdkVersion" -> type = SdkVersionType.MIN
      "targetSdkVersion" -> type = SdkVersionType.TARGET
    }

    type?.let { type ->
      if (version > versionsMap[type] ?: 0) {
        versionsMap[type] = version
      }
      foundElementMap[type] = call
    }

    if (invokedText == "maxSdkVersion" && versionsMap[SdkVersionType.MAX] ?: 11 < 11) {
      problems.add(call to message("designed.for.tablets.problem.descriptor.max"))
    }

  }

  override fun visitFile(file: GroovyFileBase) {
    super.visitFile(file)

    if (foundElementMap[SdkVersionType.TARGET] != null || foundElementMap[SdkVersionType.MIN] != null) {
      if (versionsMap[SdkVersionType.TARGET] ?: 11 < 11 && versionsMap[SdkVersionType.MIN] ?: 11 < 11) {
        foundElementMap[SdkVersionType.TARGET]?.let { problems.add(it to message("designed.for.tablets.problem.descriptor.target.or.min")) }
        foundElementMap[SdkVersionType.MIN]?.let { problems.add(it to message("designed.for.tablets.problem.descriptor.target.or.min")) }
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

private fun getVersionsFromUsesSdkElement(tag: XmlTag): MutableMap<SdkVersionType, Int> {

  fun strToInt(str: String?): Int? {
    if (str == null) return null

    try {
      val i = str.toInt()
      return i
    } catch (e: NumberFormatException) {

    }

    return null
  }

  val versionsMap = mutableMapOf<SdkVersionType, Int>()

  for (attribute in tag.attributes) {

    val version = strToInt(attribute.value) ?: continue

    when (attribute.name) {
      "android:maxSdkVersion" -> versionsMap[SdkVersionType.MAX] = version
      "android:minSdkVersion" -> versionsMap[SdkVersionType.MIN] = version
      "android:targetSdkVersion" -> versionsMap[SdkVersionType.TARGET] = version
    }
  }

  return versionsMap

}

private fun updateVersionMap(target: MutableMap<SdkVersionType, Int>, newMap: Map<SdkVersionType, Int>) {

  for (type in SdkVersionType.values()) {
    if (newMap.containsKey(type)) {
      if (!target.containsKey(type) || target[type] ?: 0 < newMap[type] ?: 0) {
        target[type] = newMap[type] ?: 0
      }
    }
  }

}


