package com.gmail.blueboxware.libgdxplugin.versions

import com.gmail.blueboxware.libgdxplugin.versions.libs.LibGDXLibrary
import com.gmail.blueboxware.libgdxplugin.versions.libs.LibGDXVersionPostfixedLibrary
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.libraries.Library
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.config.MavenComparableVersion
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrCommandArgumentList
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

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
enum class Libraries(val library: com.gmail.blueboxware.libgdxplugin.versions.Library) {

  LIBGDX(
          Library("LibGDX", "com.badlogicgames.gdx", "gdx", extKeys = listOf("gdxVersion"))
  ),
  ASHLEY(
          Library("Ashley", "com.badlogicgames.ashley", "ashley", extKeys = listOf("ashleyVersion"))
  ),
  BOX2D(
          LibGDXLibrary("Box2d", "com.badlogicgames.gdx", "gdx-box2d")
  ),
  BOX2dLIGHTS(
          Library("Box2dLights", "com.badlogicgames.box2dlights", "box2dlights", extKeys = listOf("box2DLightsVersion", "box2dlightsVersion"))
  ),
  AI(
          Library("Gdx AI", "com.badlogicgames.gdx", "gdx-ai", extKeys = listOf("aiVersion"))
  ),
  OVERLAP2D(
          Library("Overlap2D", "com.underwaterapps.overlap2druntime", "overlap2d-runtime-libgdx", extKeys = listOf("overlap2dVersion"))
  ),
  FREETYPE(
          LibGDXLibrary("GDX FreeType", "com.badlogicgames.gdx", "gdx-freetype")
  ),
  CONTROLLERS(
          LibGDXLibrary("GDX Controllers", "com.badlogicgames.gdx", "gdx-controllers")
  ),
  BULLET(
          LibGDXLibrary("GDX Bullet", "com.badlogicgames.gdx", "gdx-bullet")
  ),
  VISUI(
          Library("VisUI", "com.kotcrab.vis", "vis-ui", extKeys = listOf("visUiVersion"))
  ),
  VISRUNTIME(
          Library("Vis Runtime", "com.kotcrab.vis", "vis-runtime", extKeys = listOf("visRuntimeVersion"))
  ),
  LIBGDXUTILS(
          Library("libgdx-utils", "net.dermetfan.libgdx-utils", "libgdx-utils", extKeys = listOf("gdxUtilsVersion", "utilsVersion"))
  ),
  LIBGDXUTILS_BOX2D(
          Library("libgdx-utils-box2d", "net.dermetfan.libgdx-utils", "libgdx-utils-box2d", extKeys = listOf("utilsBox2dVersion"))
  ),
  GDXFACEBOOK(
          Library("gdx-facebook", "de.tomgrill.gdxfacebook", "gdx-facebook-core", extKeys = listOf("facebookVersion"))
  ),
  GDXDIALOGS(
          Library("gdx-dialogs", "de.tomgrill.gdxdialogs", "gdx-dialogs-core", extKeys = listOf("dialogsVersion"))
  ),
  KIWI(
          LibGDXVersionPostfixedLibrary("Kiwi", "com.github.czyzby", "gdx-kiwi", extKeys = listOf("kiwiVersion"))
  ),
  LML(
          LibGDXVersionPostfixedLibrary("gdx-lml", "com.github.czyzby", "gdx-lml", extKeys = listOf("lmlVersion"))
  ),
  LMLVIS(
          LibGDXVersionPostfixedLibrary("gdx-lml-vis", "com.github.czyzby", "gdx-lml-vis", extKeys = listOf("lmlVisVersion"))
  ),
  AUTUMN(
          LibGDXVersionPostfixedLibrary("Autumn", "com.github.czyzby", "gdx-autumn", extKeys = listOf("autumnVersion"))
  ),
  AUTUMN_MVC(
          LibGDXVersionPostfixedLibrary("Autumn MVC", "com.github.czyzby", "gdx-autumn-mvc", extKeys = listOf("autumnMvcVersion"))
  ),
  WEBSOCKET(
          LibGDXVersionPostfixedLibrary("Web Sockets", "com.github.czyzby", "gdx-websocket", extKeys = listOf("websocketVersion"))
  ),
  WEBSOCKET_SERIALIZATION(
          LibGDXVersionPostfixedLibrary("Web Sockets Serialization", "com.github.czyzby", "gdx-websocket-serialization", extKeys = listOf("websocketSerializationVersion"))
  ),
  GDXPAY(
          Library("Gdx-Pay", "com.badlogicgames.gdxpay", "gdx-pay-client", extKeys = listOf("gdxPayVersion"))
  ),
  KTX_ACTORS(
          Library("KTX actors", "com.github.czyzby", "ktx-actors", extKeys = listOf("ktxActorsVersion"))
  ),
  KTX_APP(
          Library("KTX app", "com.github.czyzby", "ktx-app")
  ),
  KTX_ASSETS(
          Library("KTX assets", "com.github.czyzby", "ktx-assets", extKeys = listOf("ktxAssetsVersion"))
  ),
  KTX_COLLECTIONS(
          Library("KTX collections", "com.github.czyzby", "ktx-collections", extKeys = listOf("ktxCollectionsVersion"))
  ),
  KTX_I18N(
          Library("KTX i18n", "com.github.czyzby", "ktx-i18n", extKeys = listOf("ktxI18nVersion"))
  ),
  KTX_INJECT(
          Library("KTX inject", "com.github.czyzby", "ktx-inject", extKeys = listOf("ktxInjectVersion"))
  ),
  KTX_LOG(
          Library("KTX log", "com.github.czyzby", "ktx-log", extKeys = listOf("ktxLogVersion"))
  ),
  KTX_MATH(
          Library("KTX math", "com.github.czyzby", "ktx-math", extKeys = listOf("ktxMathVersion"))
  ),
  KTX_SCENE2D(
          Library("KTX scene2d", "com.github.czyzby", "ktx-scene2d", extKeys = listOf("ktxScene2DVersion"))
  ),
  KTX_STYLE(
          Library("KTX style", "com.github.czyzby", "ktx-style", extKeys = listOf("ktxStyleVersion"))
  ),
  KTX_VIS(
          Library("KTX vis", "com.github.czyzby", "ktx-vis", extKeys = listOf("ktxVisVersion"))
  ),
  KTX_VIS_STYLE(
          Library("KTX vis-style", "com.github.czyzby", "ktx-vis-style", extKeys = listOf("ktxVisStyleVersion"))
  )
  ;

  companion object {

    fun extractLibraryInfoFromGroovyConstruct(psiElement: PsiElement): Pair<Libraries, MavenComparableVersion>? {

      for (lib in values()) {
        when (psiElement) {
          is GrLiteral -> lib.library.extractVersionFromLiteral(psiElement)
          is GrCommandArgumentList -> lib.library.extractVersionFromArgumentList(psiElement)
          is GrAssignmentExpression -> lib.library.extractVersionFromAssignment(psiElement)
          else -> null
        }?.let { version ->
          return Pair(lib, version)
        }
      }

      return null
    }

    fun extractLibraryInfoFromIdeaLibrary(library: Library): Pair<Libraries, MavenComparableVersion>? {

      for (libGDXLib in values()) {
        for (url in library.getUrls(OrderRootType.CLASSES)) {
          libGDXLib.library.extractVersionFromIdeaLibrary(library)?.let { version ->
            return Pair(libGDXLib, version)
          }
        }
      }

      return null

    }

    fun listOfCheckedLibraries() = values().map { it.library.name }.sortedBy(String::toLowerCase).joinToString(", ")


    fun fromExtKey(extKey: String): Libraries? {

      for (lib in values()) {
        lib.library.extKeys?.let { extKeys ->
          for (key in extKeys) {
            if (key == extKey) {
              return lib
            }
          }
        }
      }

      return null

    }

    fun fromGroovyLiteral(literal: GrLiteral): Libraries? {

      for (lib in values()) {

        for (extKey in lib.library.extKeys ?: listOf()) {
          val regex = Regex("""${lib.library.groupId}:${lib.library.artifactId}:\$$extKey""")
          if (literal.text.contains(regex)) {
            return lib
          }
        }
      }

      return null
    }

  }

  override fun toString() = library.name

}