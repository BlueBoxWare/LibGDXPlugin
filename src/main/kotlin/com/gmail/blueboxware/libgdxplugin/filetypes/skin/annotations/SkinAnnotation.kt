package com.gmail.blueboxware.libgdxplugin.filetypes.skin.annotations

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiComment

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
enum class SkinAnnotations {
  SUPPRESS
}

typealias SkinAnnotation = Pair<SkinAnnotations, String>

val NO_ANNOTATIONS = listOf<SkinAnnotation>()

val ANNOTATION_REGEX =
  Regex(
    "@\\s*(" + SkinAnnotations.values().joinToString("|") + ")\\s*\\(([^)]*)\\)",
          RegexOption.IGNORE_CASE
  )

fun PsiComment.getSkinAnnotations(): List<SkinAnnotation> {

  if (language != LibGDXSkinLanguage.INSTANCE) {
    return NO_ANNOTATIONS
  }

  val result = mutableListOf<SkinAnnotation>()

  ANNOTATION_REGEX.findAll(text).forEach { matchResult ->
    var name: SkinAnnotations? = null
    SkinAnnotations.values().forEach {
      if (matchResult.groupValues.getOrNull(1)?.toLowerCase() == it.name.toLowerCase()) {
        name = it
      }
    }
    name?.let { type ->
      matchResult.groupValues.getOrNull(2)?.let { valueString ->
        val value = StringUtil.stripQuotesAroundValue(valueString.trim()).trim()
        result.add(type to value)
      }
    }
  }

  return result

}