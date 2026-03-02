/*
 * Copyright 2026 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.findUsages

import com.gmail.blueboxware.libgdxplugin.filetypes.tree.DEFAULT_AI_IMPORTS
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.TreeFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeRecursiveVisitor
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.psi.PsiTreeTaskname
import com.gmail.blueboxware.libgdxplugin.utils.allScope
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDXProject
import com.gmail.blueboxware.libgdxplugin.utils.toPsiFile
import com.intellij.find.findUsages.CustomUsageSearcher
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runReadAction
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.search.FileTypeIndex
import com.intellij.usageView.UsageInfo
import com.intellij.usages.Usage
import com.intellij.usages.UsageInfo2UsageAdapter
import com.intellij.util.Processor

internal class DefaultImportUsageSearcher : CustomUsageSearcher() {

    override fun processElementUsages(
        element: PsiElement,
        processor: Processor<in Usage>,
        options: FindUsagesOptions
    ) {

        if (element !is PsiClass || element.name !in DEFAULT_AI_IMPORTS) return

        fun process() {
            if (!element.isValid || !element.project.isLibGDXProject()) return

            FileTypeIndex.processFiles(TreeFileType, { file ->
                file.toPsiFile(element.project)?.accept(object : PsiTreeRecursiveVisitor() {
                    override fun visitTaskname(taskName: PsiTreeTaskname) {
                        if (taskName.references.any { it.resolve() == element }) {
                            processor.process(UsageInfo2UsageAdapter(UsageInfo(taskName as PsiElement)))
                        }
                        super.visitTaskname(taskName)
                    }
                })
                return@processFiles true
            }, element.allScope())

        }

        if (ApplicationManager.getApplication().isUnitTestMode) {
            process()
        } else {
            runReadAction(::process)
        }
    }

}
