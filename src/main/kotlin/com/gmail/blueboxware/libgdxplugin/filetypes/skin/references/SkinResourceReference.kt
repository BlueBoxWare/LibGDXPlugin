package com.gmail.blueboxware.libgdxplugin.filetypes.skin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.PROPERTY_NAME_PARENT
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.PROPERTY_NAME_TINTED_DRAWABLE_NAME
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.utils.getAssociatedAtlas
import com.gmail.blueboxware.libgdxplugin.utils.isLibGDX199
import com.intellij.psi.PsiClassType
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

    element.resolveToType()?.let { valueType ->

      if (valueType.canonicalText == "com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable" && element.parent is SkinResource) {
        // Aliases for TintedDrawables are not allowed
        return PsiElementResolveResult.EMPTY_ARRAY
      }

      val skinFile = element.containingFile as? SkinFile ?: return PsiElementResolveResult.EMPTY_ARRAY

      val isTintedDrawableNameProperty = element.property?.name == PROPERTY_NAME_TINTED_DRAWABLE_NAME && element.property?.containingObject?.resolveToTypeString() == "com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable"
      val isParentProperty = element.property?.name == PROPERTY_NAME_PARENT && element.project.isLibGDX199()

      if (valueType.canonicalText ==  "com.badlogic.gdx.scenes.scene2d.utils.Drawable" || isTintedDrawableNameProperty) {
          skinFile.getResources("com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable", element.value, element).forEach {
            result.add(PsiElementResolveResult(it))
          }
      } else {
        (valueType as? PsiClassType)?.resolve()?.let { psiClass ->
          (element.containingFile as? SkinFile)?.getResources(psiClass, element.value, element, isParentProperty)?.forEach {
            result.add(PsiElementResolveResult(it))
          }
        }
      }

      if (valueType.canonicalText == "com.badlogic.gdx.scenes.scene2d.utils.Drawable" || isTintedDrawableNameProperty) {
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
