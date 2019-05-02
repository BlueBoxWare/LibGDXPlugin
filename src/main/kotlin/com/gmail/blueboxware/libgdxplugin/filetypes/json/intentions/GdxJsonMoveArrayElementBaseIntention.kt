package com.gmail.blueboxware.libgdxplugin.filetypes.json.intentions

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonArray
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonValue
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.getArrayIndexOfItem
import com.gmail.blueboxware.libgdxplugin.utils.parentWithParent
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
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
abstract class GdxJsonMoveArrayElementBaseIntention: GdxJsonBaseIntention() {

  abstract fun isAvailable(array: GdxJsonArray, index: Int): Boolean

  abstract fun invoke(editor: Editor, array: GdxJsonArray, index: Int)

  override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean =
          element.parentWithParent<GdxJsonValue, GdxJsonArray>()?.let { value ->
            (value.parent as? GdxJsonArray)?.let { array ->
              value.getArrayIndexOfItem()?.let { elementIndex ->
                array.valueList.size > 1 && isAvailable(array, elementIndex)
              }
            }
          } ?: false

  override fun invoke(project: Project, editor: Editor?, element: PsiElement) {

    element.parentWithParent<GdxJsonValue, GdxJsonArray>()?.let { value ->
      (value.parent as? GdxJsonArray)?.let { array ->
        value.getArrayIndexOfItem()?.let { index ->
          editor?.let {
            invoke(it, array, index)
          }
        }
      }
    }

  }

}