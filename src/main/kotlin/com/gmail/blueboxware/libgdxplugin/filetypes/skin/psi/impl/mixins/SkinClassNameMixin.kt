package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassName
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinElementImpl
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.references.SkinJavaClassReference
import com.gmail.blueboxware.libgdxplugin.utils.getSkinTag2ClassMap
import com.gmail.blueboxware.libgdxplugin.utils.putDollarInInnerClassName
import com.gmail.blueboxware.libgdxplugin.utils.removeDollarFromClassName
import com.intellij.lang.ASTNode
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.jetbrains.kotlin.asJava.classes.KtLightClass
import org.jetbrains.kotlin.psi.KtObjectDeclaration

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
abstract class SkinClassNameMixin(node: ASTNode): SkinClassName, SkinElementImpl(node) {

  override fun getValue() = stringLiteral.value

  override fun resolve(): PsiClass? = multiResolve().firstOrNull()

  override fun multiResolve(): List<PsiClass> {

    return CachedValuesManager.getCachedValue(this) {

      val taggedClasses: List<String>? =
              project.getSkinTag2ClassMap()?.getClassNames(value)?.takeIf { !it.isEmpty() }

      val classes: Collection<PsiClass> = ModuleUtilCore.findModuleForPsiElement(this)?.let { module ->

        JavaPsiFacade.getInstance(project)?.let { psiFacade ->

          @Suppress("IfThenToElvis")
          if (taggedClasses != null) {
            taggedClasses.flatMap { className ->
              psiFacade.findClasses(className, module.getModuleWithDependenciesAndLibrariesScope(true)).toList()
            }
          } else {
            psiFacade.findClasses(value.removeDollarFromClassName(), module.getModuleWithDependenciesAndLibrariesScope(true)).toList()
          }

        }

      } ?: listOf()

      classes.filter { clazz ->
        (clazz !is KtLightClass || clazz.kotlinOrigin !is KtObjectDeclaration) &&
                ((taggedClasses != null && taggedClasses.contains(clazz.qualifiedName)) || (taggedClasses == null && clazz.putDollarInInnerClassName() == value))
      }.map {
        it.navigationElement as? PsiClass ?: it
      }.let {
        CachedValueProvider.Result.create(it, PsiModificationTracker.MODIFICATION_COUNT)
      }

    }
  }

  override fun getReference(): SkinJavaClassReference = SkinJavaClassReference(this)

}