package com.gmail.blueboxware.libgdxplugin.versions.libs

import com.gmail.blueboxware.libgdxplugin.components.VersionManager
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.gmail.blueboxware.libgdxplugin.versions.Library
import com.gmail.blueboxware.libgdxplugin.versions.Repository
import org.jetbrains.kotlin.config.MavenComparableVersion

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
internal class LibGDXVersionPostfixedLibrary(name: String, groupId: String, artifactId: String, repository: Repository = Repository.MAVEN_CENTRAL, extKeys: List<String>? = null): Library(name, groupId, artifactId, repository, extKeys) {

  private val availableVersions = mutableListOf<String>()

  override fun getLatestVersion(versionManager: VersionManager): MavenComparableVersion? {

    versionManager.getUsedVersion(Libraries.LIBGDX)?.let { gdxVersion ->

      val regex = Regex(".*$gdxVersion(-.+)?")

      return availableVersions.filter { regex.matches(it) }.map(::MavenComparableVersion).max()

    }

    return null

  }

  override fun updateLatestVersion(versionManager: VersionManager, networkAllowed: Boolean): Boolean {


    if (networkAllowed && System.currentTimeMillis() - lastUpdated > VersionManager.SCHEDULED_UPDATE_INTERVAL) {

      fetchVersions(
              onSuccess = { versions ->
                availableVersions.clear()
                availableVersions.addAll(versions)
                lastUpdated = System.currentTimeMillis()
              },
              onFailure = {
                lastUpdated = System.currentTimeMillis()
              }
      )

      return true

    }

    return false

  }
}