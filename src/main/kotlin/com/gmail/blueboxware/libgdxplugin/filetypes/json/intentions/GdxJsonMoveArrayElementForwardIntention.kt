package com.gmail.blueboxware.libgdxplugin.filetypes.json.intentions

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonArray
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.switch
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
internal class GdxJsonMoveArrayElementForwardIntention : GdxJsonMoveArrayElementBaseIntention() {

    override fun getFamilyName(): String = "Move array element forward"

    override fun isAvailable(array: GdxJsonArray, index: Int): Boolean =
        index < array.valueList.size - 1

    override fun invoke(editor: Editor, array: GdxJsonArray, index: Int) =
        array.switch(editor, index, index + 1) ?: Unit

}
