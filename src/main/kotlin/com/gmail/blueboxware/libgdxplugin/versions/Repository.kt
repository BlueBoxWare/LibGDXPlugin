package com.gmail.blueboxware.libgdxplugin.versions


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
internal enum class Repository(val baseUrl: String) {
  MAVEN_CENTRAL("https://repo1.maven.org/maven2/"),
  JCENTER("https://jcenter.bintray.com/"),
  JITPACK("https://jitpack.io/api/builds/")
}