package com.gmail.blueboxware.libgdxplugin.filetypes.atlas2

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.psi.Atlas2Field


/*
 * Copyright 2022 Blue Box Ware
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
interface Atlas2FieldOwner : Atlas2Element {

    fun getFieldList(): List<Atlas2Field>

    fun getField(name: String): Atlas2Field?

    fun getFieldValues(name: String): List<String>?

    fun getFieldValuesI(name: String): List<Int?>?

    fun getFieldValuesI(name: String, default: Int): List<Int>?

    fun getFieldValue(name: String): String?

    fun getFieldValueI(name: String): Int?

    fun getFieldValueI(name: String, default: Int): Int

}
