package com.gmail.blueboxware.libgdxplugin.utils.androidManifest

import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag

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
enum class SdkVersionType { MIN, MAX, TARGET }

class ManifestModel {

  var supportScreens: ManifestValue<SupportsScreens>? = null

  var hasLargeScreensSupportAttribute: Boolean = false
  var hasXLargeScreenSupportAttribute: Boolean = false

  var minSDK: ManifestValue<Int> = ManifestValue(1)
  var maxSDK: ManifestValue<Int>? = null
  var targetSDK: ManifestValue<Int>? = null

  var openGLESVersion: ManifestValue<Int> = ManifestValue(0x00010000)

  val permissions: MutableList<ManifestValue<String>> = mutableListOf()

  fun resolveTargetSDK(): Int =
          targetSDK?.value ?: minSDK.value

  fun resolveSupportsScreens(): SupportsScreens =
          supportScreens?.value?.resolveSupportsScreensValues(resolveTargetSDK()) ?: SupportsScreens.getDefaultValues(resolveTargetSDK())

  fun applyExternalVersions(versionMap: Map<SdkVersionType, Int>) {
    versionMap.get(SdkVersionType.MIN)?.let { extVersion ->
      if (extVersion > minSDK.value) {
        minSDK = ManifestValue(extVersion)
      }
    }
    versionMap.get(SdkVersionType.TARGET)?.let { extVersion ->
      if (targetSDK == null || extVersion > resolveTargetSDK()) {
        targetSDK = ManifestValue(extVersion)
      }
    }
    versionMap.get(SdkVersionType.MAX)?.let { extVersion ->
      if (maxSDK == null || extVersion > maxSDK?.value ?: 0) {
        maxSDK = ManifestValue(extVersion)
      }
    }
  }

  companion object {

    fun fromFile(manifestFile: XmlFile): ManifestModel {

      val model = ManifestModel()

      manifestFile.accept(object: ManifestVisitor() {

        override fun processOpenGLESVersion(value: Int, element: XmlTag) {
          if (model.openGLESVersion.element == null || value >= model.openGLESVersion.value) {
            model.openGLESVersion = ManifestValue(value, element)
          }
        }

        override fun processMinSDKVersion(value: Int, element: XmlAttribute) {
          model.minSDK = ManifestValue(value, element)
        }

        override fun processTargetSDKVersion(value: Int, element: XmlAttribute) {
          model.targetSDK = ManifestValue(value, element)
        }

        override fun processMaxSDKVersion(value: Int, element: XmlAttribute) {
          model.maxSDK = ManifestValue(value, element)
        }

        override fun processSupportsScreens(value: SupportsScreens, element: XmlTag, hasLargeScreensSupportAttribute: Boolean, hasXLargeScreensSupportAttribute: Boolean) {
          model.supportScreens = ManifestValue(value, element)
          model.hasLargeScreensSupportAttribute = hasLargeScreensSupportAttribute
          model.hasXLargeScreenSupportAttribute = hasXLargeScreensSupportAttribute
        }

        override fun processPermission(value: String, element: XmlTag) {
          model.permissions.add(ManifestValue(value, element))
        }

      })

      return model

    }

  }

}