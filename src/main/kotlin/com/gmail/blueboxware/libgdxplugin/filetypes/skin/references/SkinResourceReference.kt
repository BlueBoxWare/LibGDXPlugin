package com.gmail.blueboxware.libgdxplugin.filetypes.skin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.getRealClassNamesAsString
import com.gmail.blueboxware.libgdxplugin.utils.getAssociatedAtlas
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.ResolveResult

/*
 * Copyright 2016 Blue Box Ware
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
class SkinResourceReference(element: SkinStringLiteral) : SkinReference<SkinStringLiteral>(element) {

  override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> {

    val result = mutableListOf<PsiElementResolveResult>()

    element.resolveToTypeString()?.let { valueType ->

      if (valueType == "com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable" && element.parent is SkinResource) {
        // Aliases for TintedDrawables are not allowed
        return PsiElementResolveResult.EMPTY_ARRAY
      }

      val isTintedDrawableNameProperty = element.property?.name == "name" && element.property?.containingObject?.resolveToTypeString() == "com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable"

      (element.containingFile as? SkinFile)?.getClassSpecifications()?.let { classSpecifications ->

          for (classSpec in classSpecifications) {
            val realClassNames = classSpec.getRealClassNamesAsString()
            if (realClassNames.contains(valueType)
                    || ((valueType == "com.badlogic.gdx.scenes.scene2d.utils.Drawable" || isTintedDrawableNameProperty) && realClassNames.contains("com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable"))
                    ) {
              for (resource in classSpec.getResourcesAsList(element)) {
                if (resource.name == element.value) {
                  result.add(PsiElementResolveResult(resource))
                }
              }
            }
          }

      }

      if (valueType == "com.badlogic.gdx.scenes.scene2d.utils.Drawable" || isTintedDrawableNameProperty) {
        element.containingFile.virtualFile?.let { virtualFile ->
          virtualFile.getAssociatedAtlas()?.let { atlasVirtualFile ->
            (element.manager.findFile(atlasVirtualFile) as? AtlasFile)?.let { atlasFile ->
              atlasFile.getPages().forEach { page ->
                page.regionList.forEach { region ->
                  if (region.name == element.value) {
                    result.add(PsiElementResolveResult(region))
                  }
                }
              }
            }
          }
        }
      }

    }

    return result.toTypedArray()
  }

  override fun handleElementRename(newElementName: String?): PsiElement {
    element.setValue(newElementName)
    return element
  }
}
