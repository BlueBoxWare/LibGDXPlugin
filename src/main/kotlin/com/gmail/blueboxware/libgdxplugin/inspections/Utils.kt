package com.gmail.blueboxware.libgdxplugin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.formatter.SkinCodeStyleSettings
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinFileImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.getOpeningBrace
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.references.AssetReference
import com.gmail.blueboxware.libgdxplugin.utils.Assets
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.impl.VirtualFileImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import com.intellij.util.indexing.FileBasedIndex
import org.jetbrains.kotlin.psi.psiUtil.startOffset


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
internal fun checkForNonExistingAssetReference(element: PsiElement, elementName: String, holder: ProblemsHolder) {

  element
          .references
          .filterIsInstance<AssetReference>()
          .takeIf { !it.isEmpty() }
          ?.firstOrNull { it.multiResolve(true).isEmpty() }
          ?.let { reference ->

            val type = reference.className ?: "<unknown>"
            val files = reference.filesPresentableText(true).takeIf { it != "" }?.let { "in $it" } ?: ""
            val fixes =
                    if (!elementName.isBlank())
                      reference.classNameWithDollar?.takeIf { it != "com.badlogic.gdx.graphics.g2d.TextureRegion" }?.let { className ->
                        reference.skinFiles.let { skinFiles ->
                          skinFiles.mapNotNull { skinFile ->
                            (skinFile.virtualFile as? VirtualFileImpl)?.id?.let { id ->
                              CreateAssetQuickFix(elementName, className, skinFile.name, id, skinFiles.size > 1)
                            }
                          }.toTypedArray()
                        }
                      }
                    else
                      null

            holder.registerProblem(element, message("nonexisting.asset.problem.descriptor", elementName, type, files), *fixes ?: arrayOf())

          }

}

private class CreateAssetQuickFix(
        val assetName: String,
        val className: String,
        val fileName: String,
        val fileId: Int,
        val showFileName: Boolean
): LocalQuickFix {

  override fun getFamilyName(): String = "Create asset '$assetName'"

  override fun getName(): String = "Create asset '$assetName'" + if (showFileName) " in '$fileName'" else ""

  override fun applyFix(project: Project, descriptor: ProblemDescriptor) {

    val virtualFile = FileBasedIndex.getInstance()?.findFileById(project, fileId) ?: return
    val skinFile = PsiManager.getInstance(project).findFile(virtualFile) as? SkinFileImpl ?: return

    val editors = FileEditorManager.getInstance(project).openFile(virtualFile, true)

    val element = if (className == "com.badlogic.gdx.scenes.scene2d.utils.Drawable") {
      skinFile.addTintedDrawable(assetName)
    } else if (className == Assets.COLOR_CLASS_NAME ) {
      skinFile.addColor(assetName)
    } else {
      skinFile.addResource(className, assetName)
    }

    @Suppress("FoldInitializerAndIfToElvis")
    if (element == null) {
      return
    }

    var position = if (className == "com.badlogic.gdx.scenes.scene2d.utils.Drawable") {
      element.text?.let { innerText ->
        val regex = Regex("""name\s*:""")
        regex.find(innerText)?.range?.endInclusive?.let { end ->
          if (CodeStyleSettingsManager.getSettings(project).getCustomSettings(SkinCodeStyleSettings::class.java).SPACE_AFTER_COLON) {
            element.startOffset + end + 1
          } else {
            element.startOffset + end
          }
        }
      }
    } else if (className == Assets.COLOR_CLASS_NAME) {
      element.text?.indexOf('#')?.let { index ->
        element.startOffset + index
      }
    } else {
      element.`object`?.getOpeningBrace()?.startOffset ?: return
    }

    if (position == null) {
      position = element.startOffset
    }

    editors.filterIsInstance<TextEditor>().firstOrNull()?.editor?.caretModel?.moveToOffset(position + 1)

  }

}