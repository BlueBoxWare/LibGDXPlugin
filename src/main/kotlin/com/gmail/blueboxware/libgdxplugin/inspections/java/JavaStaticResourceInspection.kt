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
package com.gmail.blueboxware.libgdxplugin.inspections.java

import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiField
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.search.GlobalSearchScope

class JavaStaticResourceInspection: LibGDXJavaBaseInspection() {

  override fun getStaticDescription() = message("static.resources.html.description")

  override fun getID() = "LibGDXStaticResource"

  override fun getDisplayName() = message("static.resources.inspection.name")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: JavaElementVisitor() {

    val disposableClass = JavaPsiFacade.getInstance(holder.project).findClass("com.badlogic.gdx.utils.Disposable", GlobalSearchScope.allScope(holder.project))

    override fun visitField(field: PsiField?) {

      if (field == null || disposableClass == null || !field.hasModifierProperty("static")) return

      val theType = field.type

      if (theType is PsiClassReferenceType) {
        val theClass = theType.resolve()
        if (theClass != null && theClass.isInheritor(disposableClass, true)) {
          holder.registerProblem(field, message("static.resources.problem.descriptor"))
        }
      }

    }
  }

}


