package com.gmail.blueboxware.libgdxplugin.filetypes.json.utils

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl.GdxJsonFileImpl
import com.gmail.blueboxware.libgdxplugin.utils.indexOfOrNull
import com.intellij.psi.PsiElement


/*
 * Copyright 2019 Blue Box Ware
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
fun GdxJsonElement.factory() = (containingFile as? GdxJsonFileImpl)?.factory

fun GdxJsonElement.isArrayElement() = this is GdxJsonValue && parent is GdxJsonArray

fun PsiElement.parentString(): GdxJsonString? =
        parent.let { parent ->
          when (parent) {
            is GdxJsonString -> parent
            is GdxJsonBoolean, is GdxJsonNull, is GdxJsonNumber -> parent.parent as? GdxJsonString
            else -> null
          }
        }

fun GdxJsonValue.getArrayIndexOfItem(): Int? =
        (parent as? GdxJsonArray)?.valueList?.indexOfOrNull(this)

fun GdxJsonArray.switch(index1: Int, index2: Int) =
        valueList.getOrNull(index1)?.let { element1 ->
          valueList.getOrNull(index2)?.let { element2 ->
            switch(element1, element2)
          }
        }


fun GdxJsonArray.switch(element1: GdxJsonValue, element2: GdxJsonValue) {

  if (element1.parent != this || element2.parent != this) {
    return
  }

  val element2ancher = element2.nextSibling
  element1.replace(element2)
  addBefore(element1, element2ancher)

}