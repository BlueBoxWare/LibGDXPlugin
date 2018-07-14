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
package com.gmail.blueboxware.libgdxplugin.inspections.kotlin

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.findClass
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.impl.source.PsiClassReferenceType
import org.jetbrains.kotlin.asJava.LightClassUtil
import org.jetbrains.kotlin.asJava.elements.KtLightField
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtVisitorVoid

class KotlinStaticResourceInspection : LibGDXKotlinBaseInspection() {

  override fun getStaticDescription() = message("static.resources.html.description") + message("static.resources.html.description.kotlin.note")

  override fun getID() = "LibGDXStaticResource"

  override fun getDisplayName() = message("static.resources.inspection.name")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object : KtVisitorVoid() {

    val disposableClass = holder.project.findClass("com.badlogic.gdx.utils.Disposable")

    override fun visitDeclaration(dcl: KtDeclaration) {

      if (dcl !is KtProperty || disposableClass == null) return

      val grandparent = dcl.parent.parent

      if (!dcl.isTopLevel && grandparent !is KtObjectDeclaration) return

      LightClassUtil.getLightClassBackingField(dcl)?.let { backingField ->
        (backingField as? KtLightField)?.let { field ->
          (field.type as? PsiClassReferenceType)?.let { type ->
            type.resolve()?.let { clazz ->
              if (clazz.isInheritor(disposableClass, true)) {
                holder.registerProblem(dcl, message("static.resources.problem.descriptor"))
              }
            }
          }
        }
      }


    }

  }

}
