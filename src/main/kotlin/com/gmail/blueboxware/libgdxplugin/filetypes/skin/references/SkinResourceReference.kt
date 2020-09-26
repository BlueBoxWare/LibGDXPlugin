package com.gmail.blueboxware.libgdxplugin.filetypes.skin.references

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas.AtlasFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinResource
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinStringLiteral
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.impl.source.resolve.ResolveCache

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
class SkinResourceReference(element: SkinStringLiteral): SkinReference<SkinStringLiteral>(element) {

  override fun multiResolve(incompleteCode: Boolean): Array<out ResolveResult> =
          ResolveCache.getInstance(element.project).resolveWithCaching(this, RESOLVER, false, incompleteCode)

  override fun handleElementRename(newElementName: String): PsiElement {
    newElementName.let(element::setValue)
    return element
  }

  class Resolver: ResolveCache.PolyVariantResolver<SkinResourceReference> {

    override fun resolve(resourceReference: SkinResourceReference, incompleteCode: Boolean): Array<ResolveResult> {

      val result = mutableListOf<PsiElementResolveResult>()
      val element = resourceReference.element

      element.resolveToType()?.let { valueType ->

        if (valueType.canonicalText == TINTED_DRAWABLE_CLASS_NAME && element.parent is SkinResource) {
          // Aliases for TintedDrawables are not allowed
          return PsiElementResolveResult.EMPTY_ARRAY
        }

        val skinFile = element.containingFile as? SkinFile ?: return PsiElementResolveResult.EMPTY_ARRAY

        val isTintedDrawableNameProperty =
                element.property?.name == PROPERTY_NAME_TINTED_DRAWABLE_NAME
                        && element.property?.containingObject?.resolveToTypeString() == TINTED_DRAWABLE_CLASS_NAME
        val isParentProperty = element.property?.name == PROPERTY_NAME_PARENT && element.project.isLibGDX199()
        val isFreeTypeFontGeneratorEnum = element.property?.containingObject?.resolveToTypeString() == FREETYPE_FONT_PARAMETER_CLASS_NAME
                && (valueType as? PsiClassType)?.resolve()?.isEnum == true

        if (valueType.canonicalText == DRAWABLE_CLASS_NAME || isTintedDrawableNameProperty) {
          skinFile.getResources(TINTED_DRAWABLE_CLASS_NAME, element.value, element).forEach {
            result.add(PsiElementResolveResult(it))
          }
        } else if (isFreeTypeFontGeneratorEnum) {
          (valueType as? PsiClassReferenceType)?.resolve()?.let { clazz ->
            clazz.findFieldByName(element.value, false)?.navigationElement?.let {
              result.add(PsiElementResolveResult(it))
            }
          }
        } else {
          (valueType as? PsiClassType)?.resolve()?.let { psiClass ->
            (element.containingFile as? SkinFile)?.getResources(
                    psiClass,
                    element.value,
                    element,
                    isParentProperty
            )?.forEach {
              result.add(PsiElementResolveResult(it))
            }
          }
        }

        if (valueType.canonicalText == DRAWABLE_CLASS_NAME || isTintedDrawableNameProperty) {
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

  }

  companion object {

    val RESOLVER = Resolver()

  }

}
