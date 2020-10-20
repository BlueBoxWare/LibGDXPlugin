package com.gmail.blueboxware.libgdxplugin.utils.compat

import com.intellij.util.castSafelyTo
import org.jetbrains.kotlin.idea.references.SyntheticPropertyAccessorReference
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties


/*
 * Copyright 2020 Blue Box Ware
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
fun SyntheticPropertyAccessorReference.isGetter(): Boolean {
  if (this::class.simpleName == "Getter") {
    return true
  } else if (this::class.simpleName == "Setter") {
    return false
  }
  return this::class
          .memberProperties
          .firstOrNull { it.name == "getter" }
          ?.castSafelyTo<KProperty1<SyntheticPropertyAccessorReference, Boolean>>()
          ?.get(this)
          ?: false
}
