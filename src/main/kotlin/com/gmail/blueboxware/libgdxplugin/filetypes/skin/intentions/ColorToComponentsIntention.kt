package com.gmail.blueboxware.libgdxplugin.filetypes.skin.intentions

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinObjectImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.setColor
import com.gmail.blueboxware.libgdxplugin.utils.firstParent
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement


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
internal class ColorToComponentsIntention : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getFamilyName(): String = "Convert to float components"

    override fun getText(): String = familyName

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean =
        element.firstParent<SkinObjectImpl>()?.isHexColor() == true

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        element.firstParent<SkinObject>()?.let { skinObject ->
            skinObject.asColor(true)?.let { color ->
                skinObject.setColor(color, false)
            }
        }
    }

}
