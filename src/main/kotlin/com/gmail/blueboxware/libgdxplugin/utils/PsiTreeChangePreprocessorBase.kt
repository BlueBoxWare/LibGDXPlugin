package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.lang.Language
import com.intellij.psi.*
import com.intellij.psi.impl.PsiTreeChangeEventImpl
import com.intellij.psi.impl.PsiTreeChangePreprocessor

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
abstract class PsiTreeChangePreprocessorBase(val language: Language) : PsiTreeChangePreprocessor {

    override fun treeChanged(event: PsiTreeChangeEventImpl) {

        if (event.file?.language != language) {
            return
        }

        if (PsiTreeChangeEvent.PROP_WRITABLE == event.propertyName) {
            return
        }

        val element = event.parent

        if (element == null || element.manager !is PsiManager) {
            return
        }

        var changedInsideCodeBlock = false

        @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
        when (event.code) {
            PsiTreeChangeEventImpl.PsiEventType.BEFORE_CHILDREN_CHANGE -> {
                if (element is PsiFile) {
                    changedInsideCodeBlock = true
                }
            }
            PsiTreeChangeEventImpl.PsiEventType.CHILDREN_CHANGED -> {
                if (event.isGenericChange) {
                    return
                }
                changedInsideCodeBlock = isInsideCodeBlock(event.parent)
            }
            PsiTreeChangeEventImpl.PsiEventType.BEFORE_CHILD_ADDITION,
            PsiTreeChangeEventImpl.PsiEventType.BEFORE_CHILD_REMOVAL,
            PsiTreeChangeEventImpl.PsiEventType.CHILD_ADDED,
            PsiTreeChangeEventImpl.PsiEventType.CHILD_REMOVED,
            PsiTreeChangeEventImpl.PsiEventType.BEFORE_CHILD_MOVEMENT,
            PsiTreeChangeEventImpl.PsiEventType.CHILD_REPLACED
            -> changedInsideCodeBlock = isInsideCodeBlock(event.parent)
            PsiTreeChangeEventImpl.PsiEventType.BEFORE_PROPERTY_CHANGE,
            PsiTreeChangeEventImpl.PsiEventType.PROPERTY_CHANGED
            -> changedInsideCodeBlock = false
            PsiTreeChangeEventImpl.PsiEventType.CHILD_MOVED,
            PsiTreeChangeEventImpl.PsiEventType.BEFORE_CHILD_REPLACEMENT
            -> changedInsideCodeBlock = isInsideCodeBlock(event.oldParent) && isInsideCodeBlock(event.newParent)
        }

        if (!changedInsideCodeBlock) {
            element.manager.dropPsiCaches()
        }

    }

    private fun isInsideCodeBlock(element: PsiElement?): Boolean {
        if (element is PsiFileSystemItem) {
            return false
        }
        if (element?.parent == null) {
            return true
        }
        return element.language != language
    }

}
