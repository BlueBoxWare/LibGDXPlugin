package com.gmail.blueboxware.libgdxplugin.filetypes.json.utils

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonArray
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonElement
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonValue
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl.GdxJsonFileImpl
import com.gmail.blueboxware.libgdxplugin.utils.indexOfOrNull
import com.intellij.openapi.editor.Editor


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

fun GdxJsonValue.getArrayIndexOfItem(): Int? =
    (parent as? GdxJsonArray)?.valueList?.indexOfOrNull(this)

fun GdxJsonArray.switch(editor: Editor, index1: Int, index2: Int) =
    valueList.getOrNull(index1)?.let { element1 ->
        valueList.getOrNull(index2)?.let { element2 ->
            switch(editor, element1, element2)
        }
    }


fun GdxJsonArray.switch(editor: Editor, element1: GdxJsonValue, element2: GdxJsonValue) {

    if (element1.parent != this || element2.parent != this) {
        return
    }

    val element1range = element1.textRange
    val element2range = element2.textRange

    val element1text = element1.text
    val element2text = element2.text

    if (element1range.startOffset < element2range.startOffset) {
        editor.document.replaceString(element2range.startOffset, element2range.endOffset, element1text)
        editor.document.replaceString(element1range.startOffset, element1range.endOffset, element2text)
    } else {
        editor.document.replaceString(element1range.startOffset, element1range.endOffset, element2text)
        editor.document.replaceString(element2range.startOffset, element2range.endOffset, element1text)
    }

}
