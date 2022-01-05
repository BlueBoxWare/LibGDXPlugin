package com.gmail.blueboxware.libgdxplugin

import com.gmail.blueboxware.libgdxplugin.utils.SkinTagsModificationTracker
import com.gmail.blueboxware.libgdxplugin.utils.findClasses
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDXProject
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.project.IndexNotReadyException
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClassOwner
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.psi.impl.PsiTreeChangeEventImpl
import org.jetbrains.kotlin.idea.KotlinFileType


/*
 * Copyright 2021 Blue Box Ware
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
internal class SkinTagsChangeListener(val project: Project) : PsiTreeChangeAdapter() {

    override fun childrenChanged(event: PsiTreeChangeEvent) {

        if ((event as? PsiTreeChangeEventImpl)?.isGenericChange == false) {
            return
        }
        val tracker = SkinTagsModificationTracker.getInstance(project)
        if (tracker.isFresh || !project.isLibGDXProject()) {
            return
        }
        event.file?.let { file ->
            if (file.fileType == JavaFileType.INSTANCE || file.fileType == KotlinFileType.INSTANCE) {
                if (file.text.contains("@GDXTag")) {
                    tracker.incModificationCount()
                } else {
                    val jsonClasses = try {
                        file.findClasses("com.badlogic.gdx.utils.Json")
                    } catch (e: IndexNotReadyException) {
                        return
                    }
                    (file as? PsiClassOwner)?.classes?.forEach { clazz ->
                        if (jsonClasses.any { clazz.isInheritor(it, true) }) {
                            tracker.incModificationCount()
                            return
                        }
                    }
                }
            }
        }
    }

}
