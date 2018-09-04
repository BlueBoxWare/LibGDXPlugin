package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.ElementPatternCondition
import com.intellij.patterns.InitialPatternCondition
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext


@Suppress("unused")
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
class AlwaysTruePattern: ElementPattern<PsiElement> {

  override fun accepts(o: Any?): Boolean = true

  override fun accepts(o: Any?, context: ProcessingContext?): Boolean = true

  override fun getCondition(): ElementPatternCondition<PsiElement> =
          ElementPatternCondition(object: InitialPatternCondition<PsiElement>(PsiElement::class.java) {
            override fun accepts(o: Any?, context: ProcessingContext?): Boolean = true
          })

}