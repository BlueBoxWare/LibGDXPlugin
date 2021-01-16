package com.gmail.blueboxware.libgdxplugin.inspections.global

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDXProject
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.gmail.blueboxware.libgdxplugin.versions.VersionService
import com.intellij.analysis.AnalysisScope
import com.intellij.codeInspection.GlobalInspectionContext
import com.intellij.codeInspection.GlobalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptionsProcessor
import com.intellij.openapi.components.service

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
class OutdatedVersionsInspection: GlobalInspectionTool() {

  override fun getStaticDescription() =
          message("outdated.version.inspection.static.description", Libraries.listOfCheckedLibraries())

  override fun runInspection(
          scope: AnalysisScope,
          manager: InspectionManager,
          globalContext: GlobalInspectionContext,
          problemDescriptionsProcessor: ProblemDescriptionsProcessor
  ) {

    if (!globalContext.project.isLibGDXProject()) {
      return
    }

    val versionManager = globalContext.project.service<VersionService>()

    for (library in Libraries.values()) {
      val usedVersion = versionManager.getUsedVersion(library) ?: continue
      val latestVersion = versionManager.getLatestVersion(library) ?: continue

      if (usedVersion < latestVersion) {
        problemDescriptionsProcessor.addProblemElement(
                // addProblemElement wants a reference or the problem won't be registered, so.. uhm.. yeah
                globalContext.refManager.refProject,
                manager.createProblemDescriptor(
                        message(
                                "outdated.version.inspection.msg",
                                library.library.name,
                                latestVersion
                        )
                )
        )
      }
    }

  }

}