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


