package com.gmail.blueboxware.libgdxplugin

import com.intellij.codeInspection.LocalInspectionEP
import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.PathManager
import java.io.File


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
class ShowInfo: LibGDXCodeInsightFixtureTestCase() {

  fun testShowInfo() {
    println("IntelliJ version: " + ApplicationInfo.getInstance().fullVersion)
    println("IntelliJ build: " + ApplicationInfo.getInstance().build)
    println("Kotlin version: " + KotlinVersion.CURRENT + "\n")

    println("PLUGINS:")
    PluginManager.getPlugins().sortedBy { it.name }.forEach { plugin ->
      println("\t${plugin.name}: ${plugin.version} (enabled: ${plugin.isEnabled})")
    }

    println("\nPATHS:")
    println("\tSystem: " + PathManager.getSystemPath())
    println("\tConfig: " + PathManager.getConfigPath())
    println("\tIndex: " + PathManager.getIndexRoot())
  }

  fun testCreateShortnameList() {
    val str = StringBuilder("| Suppression ID | Name | Description |\n")
    str.append("|---|---|---|\n")
    ApplicationManager
            .getApplication()
            .extensionArea
            .getExtensionPoint<LocalInspectionEP>("com.intellij.localInspection")
            .extensionList
            .filter {
              it.pluginDescriptor.pluginId?.idString == "com.gmail.blueboxware.libgdxplugin"
            }
            .sortedBy { it.shortName }
            .forEach {
              it.instantiateTool().let { tool ->
                str.append("| ${it.shortName} | ${tool.displayName} | ${tool.staticDescription} |\n")
              }
            }
    File("Inspections.md").writeText(str.toString())
  }

}