package com.gmail.blueboxware.libgdxplugin.utils

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasFile
import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.LibGDXAtlasLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.intellij.lang.Language
import com.intellij.lang.properties.psi.impl.PropertiesFileImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileVisitor
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiFileImpl
import org.jetbrains.kotlin.psi.KtCallExpression
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

val SKIN_TEXTURE_REGION_METHODS = listOf("getRegion", "getRegions", "getPatch", "getSprite", "getTiledDrawable")

val TEXTURE_ATLAS_TEXTURE_METHODS = listOf("createPatch", "createSprite", "createSprites", "findRegion", "findRegions")

const val ASSET_ANNOTATION_NAME = "com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets"
const val TAG_ANNOTATION_NAME = "com.gmail.blueboxware.libgdxplugin.annotations.GDXTag"

const val ASSET_ANNOTATION_SKIN_PARAM_NAME = "skinFiles"
const val ASSET_ANNOTATION_ATLAS_PARAM_NAME = "atlasFiles"
const val ASSET_ANNOTATION_PROPERTIES_PARAM_NAME = "propertiesFiles"

const val COLOR_CLASS_NAME = "com.badlogic.gdx.graphics.Color"
const val COLORS_CLASS_NAME = "com.badlogic.gdx.graphics.Colors"
const val OBJECT_MAP_CLASS_NAME = "com.badlogic.gdx.utils.ObjectMap"

const val DRAWABLE_CLASS_NAME = "com.badlogic.gdx.scenes.scene2d.utils.Drawable"
const val TINTED_DRAWABLE_CLASS_NAME = "com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable"
const val TEXTURE_REGION_CLASS_NAME = "com.badlogic.gdx.graphics.g2d.TextureRegion"
const val BITMAPFONT_CLASS_NAME = "com.badlogic.gdx.graphics.g2d.BitmapFont"

const val SKIN_CLASS_NAME = "com.badlogic.gdx.scenes.scene2d.ui.Skin"
const val TEXTURE_ATLAS_CLASS_NAME = "com.badlogic.gdx.graphics.g2d.TextureAtlas"
const val I18NBUNDLE_CLASS_NAME = "com.badlogic.gdx.utils.I18NBundle"

val TARGETS_FOR_GDXANNOTATION = listOf(SKIN_CLASS_NAME, TEXTURE_ATLAS_CLASS_NAME, I18NBUNDLE_CLASS_NAME)

val SKIN_TEXTURE_REGION_CLASSES = listOf(
        TEXTURE_REGION_CLASS_NAME,
        "com.badlogic.gdx.graphics.g2d.NinePatch",
        "com.badlogic.gdx.graphics.g2d.Sprite"
)

val I18NBUNDLE_PROPERTIES_METHODS = listOf("format", "get")

val FAKE_FILE_KEY = key<Boolean>("com.gmail.blueboxware.libgdxplugin.fake")

val NO_ASSET_FILES: Pair<List<SkinFile>, List<AtlasFile>> = Pair(listOf(), listOf())

internal fun VirtualFile.getAssociatedFiles(): List<VirtualFile> {

  val result = mutableListOf<VirtualFile>()

  VfsUtil.visitChildrenRecursively(parent, object: VirtualFileVisitor<Unit>() {
    override fun visitFile(file: VirtualFile): Boolean {
      if (!file.isDirectory && file != this@getAssociatedFiles) {
        result.add(file)
      }

      return true
    }
  })

  return result
}

internal fun VirtualFile.getAssociatedAtlas(): VirtualFile? {

  val dot = name.lastIndexOf('.')
  if (dot > -1) {
    val atlasName = name.substring(0, dot) + ".atlas"
    return parent?.findChild(atlasName)
  }

  return null
}

internal fun VirtualFile.readImageNamesFromAtlas(): List<String> {

  val result = mutableListOf<String>()

  try {
    inputStream.use {
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

private fun createFakePsiFile(name: String, project: Project, original: PsiFile?, language: Language): PsiFile? =
        PsiFileFactory.getInstance(project).createFileFromText(name, language, original?.text ?: "")?.apply {
          putUserData(FAKE_FILE_KEY, true)
          virtualFile.isWritable = false
          original?.let { original ->
            if (this is PsiFileImpl) originalFile = original
          }
        }

private fun getAssetPsiFiles(project: Project, skinFileNames: List<String>, atlasFileNames: List<String>): Pair<List<SkinFile>, List<AtlasFile>> {

  val skinFiles = mutableListOf<SkinFile>()
  val atlasFiles = mutableListOf<AtlasFile>()

  for (skinFileName in skinFileNames) {
    if (skinFileName != "") {
      var skinFile = project.getPsiFile(skinFileName)
      if (skinFile != null && skinFile !is SkinFile) {
        skinFile = createFakePsiFile(skinFileName, project, skinFile, LibGDXSkinLanguage.INSTANCE)
      }
      (skinFile as? SkinFile)?.let { skinFiles.add(it) }
    }
  }

  val calculatedAtlasFilenames =
          skinFileNames.mapNotNull {
            it.lastIndexOf('.').let { dot ->
              if (dot > -1) {
                it.substring(0, dot) + ".atlas"
              } else {
                null
              }
            }
          }

  for (atlasFileName in atlasFileNames + calculatedAtlasFilenames) {
    if (atlasFileName != "") {
      var atlasFile = project.getPsiFile(atlasFileName)
      if (atlasFile != null && atlasFile !is AtlasFile) {
        atlasFile = createFakePsiFile(atlasFile.name, project, atlasFile, LibGDXAtlasLanguage.INSTANCE)
      }
      (atlasFile as? AtlasFile)?.let { atlasFiles.add(it) }
    }
  }


  return Pair(skinFiles, atlasFiles)

}

private fun getAssetFilesFromAnnotation(project: Project, annotation: AnnotationWrapper?): Pair<List<SkinFile>, List<AtlasFile>> {

  if (annotation != null) {
    val skins = annotation.getValue(ASSET_ANNOTATION_SKIN_PARAM_NAME)
    val atlasses = annotation.getValue(ASSET_ANNOTATION_ATLAS_PARAM_NAME)
    return getAssetPsiFiles(project, skins, atlasses)
  }

  return NO_ASSET_FILES

}

private fun findAssetsAnnotationClass(context: PsiElement): PsiClass? =
        context.findClass(ASSET_ANNOTATION_NAME)

internal fun PsiMethodCallExpression.getAssetFiles(): Pair<List<SkinFile>, List<AtlasFile>> {

  findAssetsAnnotationClass(this)?.let { annotation ->
    return getAssetFilesFromAnnotation(project, getAnnotation(annotation))
  }

  return NO_ASSET_FILES
}

internal fun KtCallExpression.getAssetFiles(): Pair<List<SkinFile>, List<AtlasFile>> {

  findAssetsAnnotationClass(this)?.let { annotation ->
    return getAssetFilesFromAnnotation(project, getAnnotation(annotation))
  }

  return NO_ASSET_FILES

}

internal fun PsiMethodCallExpression.getPropertiesFiles(): List<String> {

  findAssetsAnnotationClass(this)?.let { annotationClass ->
    getAnnotation(annotationClass)?.let { annotation ->
      return annotation.getValue(ASSET_ANNOTATION_PROPERTIES_PARAM_NAME)
    }
  }

  return listOf()

}

internal fun KtCallExpression.getPropertiesFiles(): List<String> {

  findAssetsAnnotationClass(this)?.let { annotationClass ->
    getAnnotation(annotationClass)?.let { annotation ->
      return annotation.getValue(ASSET_ANNOTATION_PROPERTIES_PARAM_NAME)
    }
  }

  return listOf()

}

internal fun PropertiesFileImpl.isDefaultFile(): Boolean = this == resourceBundle.defaultPropertiesFile