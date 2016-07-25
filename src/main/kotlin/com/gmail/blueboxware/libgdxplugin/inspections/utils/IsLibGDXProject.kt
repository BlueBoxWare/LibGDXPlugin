package com.gmail.blueboxware.libgdxplugin.inspections.utils

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project

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

private val keyPrefix = "com.gmail.blueboxware.libgdxplugin."
val projectUrlKey = keyPrefix + "projectPath"
val isLibGDXProjectKey = keyPrefix + "isLibGDXProject"

fun isLibGDXProject(project: Project): Boolean {

  val propertiesComponent = PropertiesComponent.getInstance()

  if (propertiesComponent.getValue(projectUrlKey) == project.presentableUrl) {
    return propertiesComponent.getBoolean(isLibGDXProjectKey)
  }

  val isLibGDXProject = getLibraryVersion(GDXLibrary.GDX, project) != null

  propertiesComponent.setValue(projectUrlKey, project.presentableUrl)
  propertiesComponent.setValue(isLibGDXProjectKey, isLibGDXProject)

  return isLibGDXProject

}