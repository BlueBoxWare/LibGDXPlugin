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

fun getTestDataPathFromProperty(): String {
  val path = System.getProperty("libgdxplugin.test.path")
  if (path == null) {
    throw AssertionError("Use -Dlibgdxplugin.test.path=<TESTPATH> to specify the absolute path to the testdata directory")
  }
  return path
}