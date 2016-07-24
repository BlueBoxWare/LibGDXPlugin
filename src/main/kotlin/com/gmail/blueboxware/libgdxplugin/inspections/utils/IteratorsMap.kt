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

import com.gmail.blueboxware.libgdxplugin.message
import java.util.*

val iteratorsMap: Map<String, List<String>> by lazy {
  val iteratorMap = mutableMapOf<String, List<String>>()
  val classNamesTokenizer = StringTokenizer(message("collection.classes"), ";")

  while (classNamesTokenizer.hasMoreTokens()) {
    val className = "com.badlogic.gdx.utils." + classNamesTokenizer.nextToken()
    val methodNamesTokenizer = StringTokenizer(message(className + ".iterators"), ";")
    val methodNames = mutableListOf<String>()

    while (methodNamesTokenizer.hasMoreTokens()) {
      methodNames.add(methodNamesTokenizer.nextToken())
    }

    iteratorMap.put(className, methodNames)
  }

  return@lazy iteratorMap
}

