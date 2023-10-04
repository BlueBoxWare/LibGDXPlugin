package com.gmail.blueboxware.libgdxplugin.annotators

import com.gmail.blueboxware.libgdxplugin.utils.GutterColorRenderer
import com.gmail.blueboxware.libgdxplugin.utils.getColor
import com.intellij.codeHighlighting.TextEditorHighlightingPass
import com.intellij.codeHighlighting.TextEditorHighlightingPassFactory
import com.intellij.codeHighlighting.TextEditorHighlightingPassFactoryRegistrar
import com.intellij.codeHighlighting.TextEditorHighlightingPassRegistrar
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.ex.dummy.DummyFileSystem
import com.intellij.psi.PsiCompiledFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiRecursiveElementVisitor
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import java.awt.Color


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
internal class ColorAnnotationsHighlightingPassFactory :
    TextEditorHighlightingPassFactory,
    TextEditorHighlightingPassFactoryRegistrar {

    override fun registerHighlightingPassFactory(registrar: TextEditorHighlightingPassRegistrar, project: Project) {
        registrar.registerTextEditorHighlightingPass(this, null, null, true, -1)
    }

    override fun createHighlightingPass(file: PsiFile, editor: Editor): TextEditorHighlightingPass? {

        if (file.fileType != JavaFileType.INSTANCE) {
            return null
        }

        if (file.originalFile !is PsiCompiledFile &&
            (file.virtualFile.fileSystem !is DummyFileSystem || file.name.endsWith("decompiled.java"))
        ) {
            return null
        }

        return object : TextEditorHighlightingPass(file.project, editor.document) {

            val annotations = mutableListOf<Pair<PsiElement, Color>>()

            override fun doCollectInformation(progress: ProgressIndicator) {

                file.accept(object : PsiRecursiveElementVisitor() {
                    override fun visitElement(element: PsiElement) {
                        super.visitElement(element)

                        element.getColor()?.let { color ->
                            annotations.add(element to color)
                        }

                        if (progress.isCanceled) {
                            throw ProcessCanceledException()
                        }
                    }
                })

            }

            override fun doApplyInformationToEditor() {

                for (highlighter in editor.markupModel.allHighlighters) {
                    if (highlighter.gutterIconRenderer is GutterColorRenderer) {
                        editor.markupModel.removeHighlighter(highlighter)
                    }
                }

                annotations.forEach { (element, color) ->
                    editor.markupModel.addRangeHighlighter(
                        element.startOffset,
                        element.endOffset,
                        HighlighterLayer.ADDITIONAL_SYNTAX,
                        null,
                        HighlighterTargetArea.EXACT_RANGE
                    ).gutterIconRenderer = GutterColorRenderer(color)
                }

            }
        }

    }

}
