package com.gmail.blueboxware.libgdxplugin.filetypes.skin.findUsages

import com.gmail.blueboxware.libgdxplugin.utils.isLibGDXProject
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.UseScopeEnlarger
import org.jetbrains.kotlin.idea.base.util.projectScope
import org.jetbrains.kotlin.psi.KtClass

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
internal class ClassUseScopeEnlarger : UseScopeEnlarger() {

    override fun getAdditionalUseScope(element: PsiElement): SearchScope? {

        if (element !is PsiClass && element !is KtClass) return null

        if (!element.project.isLibGDXProject()) return null

        if (element is PsiClass) {
            if (element.containingClass != null && !element.hasModifierProperty(PsiModifier.STATIC)) return null
        } else if ((element as? KtClass)?.isInner() == true) {
            return null
        }

        return element.project.projectScope()


    }

}
