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
package com.gmail.blueboxware.libgdxplugin.inspections.utils

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.impl.libraries.ProjectLibraryTable
import com.intellij.psi.PsiElement
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementVisitor
import org.kohsuke.github.GitHub
import org.kohsuke.github.GitHubBuilder
import org.kohsuke.github.RateLimitHandler
import java.io.IOException
import java.util.*

enum class GDXLibrary  { GDX, BOX2DLIGHTS, ASHLEY, AI, OVERLAP2D  }

val repoMap: Map<GDXLibrary, String> = mapOf(
    GDXLibrary.GDX to "libgdx/libgdx",
    GDXLibrary.BOX2DLIGHTS to "libgdx/box2dlights",
    GDXLibrary.ASHLEY to "libgdx/ashley",
    GDXLibrary.AI to "libgdx/gdx-ai",
    GDXLibrary.OVERLAP2D to "UnderwaterApps/overlap2d-runtime-libgdx"
)

val gradleExtNameMap: Map<String, GDXLibrary> = mapOf(
    "gdxVersion" to GDXLibrary.GDX,
    "box2DLightsVersion" to GDXLibrary.BOX2DLIGHTS,
    "ashleyVersion" to GDXLibrary.ASHLEY,
    "aiVersion" to GDXLibrary.AI
)

val mavenCoordMap: Map<GDXLibrary, String> = mapOf(
    GDXLibrary.GDX to "com.badlogicgames.gdx:gdx",
    GDXLibrary.BOX2DLIGHTS to "com.badlogicgames.box2dlights:box2dlights",
    GDXLibrary.ASHLEY to "com.badlogicgames.ashley:ashley",
    GDXLibrary.AI to "com.badlogicgames.gdx:gdx-ai",
    GDXLibrary.OVERLAP2D to "com.underwaterapps.overlap2druntime:overlap2d-runtime-libgdx"
)

val versionStringRegex = Regex("[0-9]+(\\.[0-9]+)*")

fun compareVersionStrings(string1: String, string2: String): Int {

  if (!string1.matches(versionStringRegex) || !string2.matches(versionStringRegex)) throw  IllegalArgumentException("Invalid version string format")

  val parts1 = string1.split(".")
  val parts2 = string2.split(".")

  val length = Math.max(parts1.size, parts2.size)

  for (i in 0..length - 1) {
    val part1 = if (i < parts1.size) Integer.parseInt(parts1[i]) else 0
    val part2 = if (i < parts2.size) Integer.parseInt(parts2[i]) else 0

    if (part1 < part2) return -1
    if (part1 > part2) return 1
  }

  return 0

}

fun extractInfoFromMavenCoord(coord: String): Pair<GDXLibrary, String>? {

  for (gdxLib in GDXLibrary.values()) {
      val regex = Regex("${mavenCoordMap[gdxLib]}:($versionStringRegex)")
      val matchResult = regex.find(coord)
      matchResult?.let { result ->
        return Pair(gdxLib, matchResult.groupValues[1])
      }
  }

  return null

}

fun getLibraryVersion(library: GDXLibrary, project: Project): String? {

  val regex = Regex("${mavenCoordMap[library]}:($versionStringRegex)")

  for (lib in ProjectLibraryTable.getInstance(project).libraryIterator) {
    lib.name?.let { name ->
      val matchResult = regex.find(name)
      if (matchResult?.groupValues?.get(1) != null) {
        return matchResult?.groupValues?.get(1)
      }
    }
  }

  // not found in project library table, check build.gradle files manually

  val files = FilenameIndex.getFilesByName(project, "build.gradle", GlobalSearchScope.allScope(project))

  var versionFound: String? = null

  for (file in files) {
    file.accept(GroovyPsiElementVisitor(object: GradleBuildFileVersionsVisitor() {

      override fun onVersionFound(lib: GDXLibrary, version: String, element: PsiElement) {
        if (lib == library) {
          versionFound = version
        }
      }
    }))

    if (versionFound != null) return versionFound
  }

  return null

}

object GitHub {
  private var gitHub: GitHub? = null

  private var propertiesComponent: PropertiesComponent? = null
  private var keyPrefix = "com.gmail.blueboxware.libgdxplugin."

  private fun currentHour() = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

  // for testing only
  fun setPropertiesComponent(propertiesComponent: PropertiesComponent) {
    this.propertiesComponent = propertiesComponent
  }

  fun getLatestVersion(lib: GDXLibrary, loggerId: String? = null, forceFromCache: Boolean = false /* for testing  only */): String? {

    if (propertiesComponent == null) propertiesComponent = PropertiesComponent.getInstance()

    propertiesComponent?.let { propertiesComponent ->
      val cachedHour = propertiesComponent.getInt(keyPrefix + "hour", 24)

      if (cachedHour != currentHour() && !forceFromCache) {
        propertiesComponent.setValue(keyPrefix + "hour", currentHour(), 24)
        for (l in GDXLibrary.values()) {
          propertiesComponent.unsetValue(keyPrefix + l.name)
        }
      } else {
        val cachedVersion = propertiesComponent.getValue(keyPrefix + lib.name)
        if (cachedVersion != null) {
          return cachedVersion
        }
      }

      try {

        if (gitHub == null) {
          gitHub = GitHubBuilder().withRateLimitHandler(RateLimitHandler.FAIL).build()
        }

        gitHub?.let { gitHub ->

          val repository = gitHub.getRepository(repoMap[lib])
          val tags = repository.listTags().asList()

          var latest: String? = null

          for (tag in tags) {
            var name = tag.name

            if (name.matches(Regex(".*-[0-9]+(\\.[0-9]+)*"))) {
              name = name.substring(name.lastIndexOf("-") + 1, name.length)
            }

            if (name.matches(versionStringRegex)) {
              if (latest == null || compareVersionStrings(name, latest) > 0) {
                latest = name
              }
            }
          }

          latest?.let {
            propertiesComponent.setValue(keyPrefix + lib.name, latest)
          }

          return latest

        }

      } catch (e: IOException) {
        val logger = if (loggerId != null) Logger.getInstance("#$loggerId") else null
        logger?.warn("Could not get version information for ${repoMap[lib]} from GitHub: " + e.message)
        propertiesComponent.setValue(keyPrefix + lib.name, "0")
      }

    }

    return null

  }

}
