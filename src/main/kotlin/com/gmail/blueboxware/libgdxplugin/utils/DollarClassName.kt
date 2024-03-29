package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.psi.PsiClass


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
class DollarClassName(val dollarName: String) {

    constructor(clazz: PsiClass) : this(clazz.putDollarInInnerClassName())

    val plainName = dollarName.replace('$', '.')

    companion object {
        private fun PsiClass.putDollarInInnerClassName(): String =
            containingClass?.let {
                it.putDollarInInnerClassName() + "$" + name
            } ?: qualifiedName ?: ""
    }

    override fun equals(other: Any?): Boolean = dollarName == (other as? DollarClassName)?.dollarName

    override fun hashCode(): Int = dollarName.hashCode()

    override fun toString(): String = plainName

}
