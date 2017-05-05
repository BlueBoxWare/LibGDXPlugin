package com.gmail.blueboxware.libgdxplugin.utils.androidManifest

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
class SupportsScreens {

  var resizeable: Boolean? = null
  var anyDensity: Boolean? = null
  var smallScreens: Boolean? = null
  var normalScreens: Boolean? = null
  var largeScreens: Boolean? = null
  var xlargeScreens: Boolean? = null

  fun resolveSupportsScreensValues(targetSdkVersion: Int): SupportsScreens {

    val result = getDefaultValues(targetSdkVersion)

    result.resizeable = resizeable ?: result.resizeable
    result.anyDensity = anyDensity ?: result.anyDensity
    result.smallScreens = smallScreens ?: result.smallScreens
    result.normalScreens = normalScreens ?: result.normalScreens
    result.largeScreens = largeScreens ?: result.largeScreens
    result.xlargeScreens = xlargeScreens ?: result.xlargeScreens

    return result

  }

  companion object {

    fun getDefaultValues(targetSdkVersion: Int): SupportsScreens {

      val result = SupportsScreens()

      result.normalScreens = true

      result.resizeable = targetSdkVersion <= 3
      result.anyDensity = targetSdkVersion <= 3
      result.smallScreens = targetSdkVersion <= 3
      result.largeScreens = targetSdkVersion <= 3
      result.xlargeScreens = targetSdkVersion <= 3

      return result

    }

  }

}