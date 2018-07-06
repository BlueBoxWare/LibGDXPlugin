package com.gmail.blueboxware.libgdxplugin.utils


/*
 * Copyright 2018 Blue Box Ware
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
class TagMap {

  private val map = mutableMapOf<String, MutableList<String>>()

  fun add(tagName: String, className: String) {
    val entry = map[tagName]

    if (entry == null) {
      map[tagName] = mutableListOf(className)
    } else {
      entry.add(className)
    }
  }

  fun addAll(tags: Collection<Pair<String, String>>) =
          tags.forEach { (tagName, className) ->
            add(tagName, className)
          }

  fun getClassNames(tag: String): List<String>? = map[tag]

  fun getTags(className: String? = null): Set<String> =
          map.filter { className == null || it.value.contains(className) }.keys

}