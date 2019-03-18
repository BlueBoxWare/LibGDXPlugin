package com.gmail.blueboxware.libgdxplugin.filetypes.properties

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.lang.properties.references.PropertiesCompletionContributor
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ArrayUtil
import com.intellij.util.ProcessingContext


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
class GDXPropertiesCompletionContributor: CompletionContributor() {

  init {
    extend(null, PlatformPatterns.psiElement(), object: CompletionProvider<CompletionParameters>() {

      override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {

        val position = parameters.position.context ?: return
        val references = ArrayUtil.mergeArrays(position.references, position.context?.references ?: arrayOf())

        val startOffset = parameters.offset

        references.forEach { reference ->
          if (reference is GDXPropertyReference) {
            val element = reference.element
            val offsetInElement = startOffset - element.textRange.startOffset
            val range = reference.rangeInElement
            if (offsetInElement >= range.startOffset) {
              val prefix = element.text.substring(range.startOffset, offsetInElement)
              val variants = PropertiesCompletionContributor.getVariants(reference)
              result.withPrefixMatcher(prefix).addAllElements(variants.toMutableList())
            }
          }
        }

      }

    })
  }

}