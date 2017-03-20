package com.gmail.blueboxware.libgdxplugin.utils

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasFile
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.LibGDXAtlasLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.utils.PsiUtils.getPsiFile
import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileVisitor
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiFileImpl
import org.jetbrains.kotlin.idea.caches.resolve.analyzeFully
import org.jetbrains.kotlin.idea.references.KtSimpleNameReference
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.plainContent
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getCalleeExpressionIfAny
import java.io.IOException

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
object AssetUtils {

  val SKIN_TEXTURE_REGION_CLASSES = listOf(
          "com.badlogic.gdx.graphics.g2d.TextureRegion",
          "com.badlogic.gdx.graphics.g2d.NinePatch",
          "com.badlogic.gdx.graphics.g2d.Sprite"
  )
  val SKIN_TEXTURE_REGION_METHODS = listOf("getRegion", "getRegions", "getPatch", "getSprite", "getTiledDrawable")

  val ASSET_ANNOTATION_NAME = "com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets"

  val ASSET_ANNOTATION_SKIN_PARAM_NAME = "skinFiles"
  val ASSET_ANNOTATION_ATLAS_PARAM_NAME = "atlasFiles"

  val SKIN_CLASS_NAME = "com.badlogic.gdx.scenes.scene2d.ui.Skin"

  val FAKE_FILE_KEY = Key<Boolean>("com.gmail.blueboxware.libgdxplugin.fake")

  private val NO_ASSET_FILES: Pair<List<SkinFile>, List<AtlasFile>> = Pair(listOf(), listOf())

  fun getAssociatedFiles(virtualFile: VirtualFile): List<VirtualFile> {

    val result = mutableListOf<VirtualFile>()

    VfsUtil.visitChildrenRecursively(virtualFile.parent, object : VirtualFileVisitor<Unit>() {
      override fun visitFile(file: VirtualFile): Boolean {
        if (!file.isDirectory && file != virtualFile) {
          result.add(file)
        }

        return true
      }
    })

    return result
  }

  fun getAssociatedAtlas(file: VirtualFile): VirtualFile? {

    val dot = file.name.lastIndexOf('.')
    if (dot > -1) {
      val atlasName = file.name.substring(0, dot) + ".atlas"
      return file.parent?.findChild(atlasName)
    }

    return null
  }

  fun readImageNamesFromAtlas(file: VirtualFile): List<String> {

    val result = mutableListOf<String>()

    try {
      file.inputStream.use {
        var previousLine = ""

        it.reader().forEachLine { line ->
          if (line.firstOrNull() == ' ' && previousLine.firstOrNull() != ' ') {
            result.add(previousLine)
          }
          previousLine = line
        }

      }
    } catch (e: IOException) {
      // Do nothing
    }

    return result
  }

  private fun createFakePsiFile(project: Project, original: PsiFile?, language: Language): PsiFile? =
          PsiFileFactory.getInstance(project).createFileFromText(language, original?.text ?: "")?.apply {
            putUserData(FAKE_FILE_KEY, true)
            virtualFile.isWritable = false
            original?.let { original ->
              if (this is PsiFileImpl) originalFile = original
            }
          }

  private fun getAssetFiles(project: Project, skinFileNames: List<String>, atlasFileNames: List<String>?): Pair<List<SkinFile>, List<AtlasFile>> {

    val skinFiles = mutableListOf<SkinFile>()
    val atlasFiles = mutableListOf<AtlasFile>()

    for (skinFileName in skinFileNames) {
      if (skinFileName != "") {
        var skinFile = getPsiFile(project, skinFileName)
        if (skinFile != null && skinFile !is SkinFile) {
          skinFile = createFakePsiFile(project, skinFile, LibGDXSkinLanguage.INSTANCE)
        }
        (skinFile as? SkinFile)?.let { skinFiles.add(it) }
      }
    }

    val calculatedAtlasFilenames = atlasFileNames ?:
              skinFileNames.mapNotNull {
                it.lastIndexOf('.').let { dot ->
                  if (dot > -1) {
                    it.substring(0, dot) + ".atlas"
                  } else {
                    null
                  }
                }
              }

    for (atlasFileName in calculatedAtlasFilenames) {
      if (atlasFileName != "") {
        var atlasFile = getPsiFile(project, atlasFileName)
        if (atlasFile != null && atlasFile !is AtlasFile) {
          atlasFile = createFakePsiFile(project, atlasFile, LibGDXAtlasLanguage.INSTANCE)
        }
        (atlasFile as? AtlasFile)?.let { atlasFiles.add(it) }
      }
    }


    return Pair(skinFiles, atlasFiles)

  }

  private fun getAssetFiles(annotation: PsiAnnotation): Pair<List<SkinFile>, List<AtlasFile>>? {

    if (annotation.qualifiedName != ASSET_ANNOTATION_NAME) {
      return null
    }

    var skinFileNames =
            (annotation.findAttributeValue(ASSET_ANNOTATION_SKIN_PARAM_NAME) as? PsiArrayInitializerMemberValue)?.initializers?.mapNotNull {
              (it as? PsiLiteralExpression)?.value as? String
            }?.filter { it != "" }
    if (skinFileNames == null) {
      skinFileNames = ((annotation.findAttributeValue(ASSET_ANNOTATION_SKIN_PARAM_NAME) as? PsiLiteralExpression)?.value as? String)?.let {
        listOf(it)
      } ?: listOf()
    }

    var atlasFileNames =
            (annotation.findDeclaredAttributeValue(ASSET_ANNOTATION_ATLAS_PARAM_NAME) as? PsiArrayInitializerMemberValue)?.initializers?.mapNotNull {
              (it as? PsiLiteralExpression)?.value as? String
            }?.filter { it != "" }
    if (atlasFileNames == null) {
      atlasFileNames = ((annotation.findDeclaredAttributeValue(ASSET_ANNOTATION_ATLAS_PARAM_NAME) as? PsiLiteralExpression)?.value as? String)?.let {
        listOf(it)
      }
    }

    return getAssetFiles(annotation.project, skinFileNames, atlasFileNames)

  }

  private fun getAssetFiles(modifierList: KtModifierList): Pair<List<SkinFile>, List<AtlasFile>>? {

    val skinFiles = mutableListOf<String>()
    var atlasFiles: MutableList<String>? = null

    modifierList.annotationEntries.forEach { entry ->
      modifierList.analyzeFully().get(BindingContext.ANNOTATION, entry)?.type?.getJetTypeFqName(false)?.let { fqName ->
        if (fqName == ASSET_ANNOTATION_NAME) {

          entry.valueArgumentList?.arguments?.forEach { argument ->
            val name = argument.getArgumentName()?.asName?.identifier
            val value = (argument.getArgumentExpression() as? KtCallExpression)?.valueArgumentList?.arguments?.mapNotNull {
              (it.getArgumentExpression() as? KtStringTemplateExpression)?.plainContent
            }

            if (value != null) {
              when (name) {
                ASSET_ANNOTATION_SKIN_PARAM_NAME -> skinFiles.addAll(value)
                ASSET_ANNOTATION_ATLAS_PARAM_NAME -> {
                  if (atlasFiles == null) {
                    atlasFiles = mutableListOf()
                  }
                  atlasFiles?.addAll(value)
                }
              }
            }
          }

          return getAssetFiles(modifierList.project, skinFiles, atlasFiles)

        }
      }
    }

    return null

  }

  fun getAssetFiles(element: KtSimpleNameReference): Pair<List<SkinFile>, List<AtlasFile>>? {

    element.resolve()?.let { result ->
      (result as? KtProperty)?.modifierList?.let { modifierList ->
        return getAssetFiles(modifierList)
      }
      (result as? PsiField)?.modifierList?.let { modifierList ->
        modifierList.findAnnotation(ASSET_ANNOTATION_NAME)?.let { annotation ->
          return getAssetFiles(annotation)
        }
      }
    }

    return null

  }

  fun getAssetFiles(element: PsiMethodCallExpression): Pair<List<SkinFile>, List<AtlasFile>> {

    element.methodExpression.qualifierExpression?.let { qualifierExpression ->
      ((qualifierExpression as? PsiMethodCallExpression)?.resolveMethod()?.navigationElement as? KtProperty)?.modifierList?.let { modifierList ->
        getAssetFiles(modifierList)?.let {
          return it
        }
      }
      ((qualifierExpression as? PsiReference)?.resolve() as? PsiVariable)?.modifierList?.findAnnotation(ASSET_ANNOTATION_NAME)?.let { annotation ->
        getAssetFiles(annotation)?.let {
          return it
        }
      }
    }

    return NO_ASSET_FILES
  }

  fun getAssetFiles(element: KtCallExpression): Pair<List<SkinFile>, List<AtlasFile>> {

    (element.context as? KtDotQualifiedExpression)?.receiverExpression?.getCalleeExpressionIfAny()?.references?.forEach {
      if (it is KtSimpleNameReference) {
        getAssetFiles(it)?.let {
          return it
        }
      }
    }

    return NO_ASSET_FILES

  }

}