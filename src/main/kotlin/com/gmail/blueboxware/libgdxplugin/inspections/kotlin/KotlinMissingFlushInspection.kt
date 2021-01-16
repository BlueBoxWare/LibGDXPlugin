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
import com.gmail.blueboxware.libgdxplugin.utils.resolveCallToStrings
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.search.searches.ClassInheritorsSearch
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.psi.*

class KotlinMissingFlushInspection: LibGDXKotlinBaseInspection() {

  override fun getStaticDescription() = message("missing.flush.html.description")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: KtVisitorVoid() {

    override fun visitBlockExpression(expression: KtBlockExpression) {

      super.visitBlockExpression(expression)

      if (
              expression.parent !is KtNamedFunction
              && expression.parent !is KtClassInitializer
              && expression.parent !is KtFunctionLiteral
      ) return

      val methodChecker = MissingFlushInspectionMethodChecker(getPreferenceSubClasses(holder.project))

      expression.accept(methodChecker)

      methodChecker.lastPreferenceChange?.let { lastPreferenceChange ->

        if (holder.results.none { it.psiElement == lastPreferenceChange }) {
          holder.registerProblem(lastPreferenceChange, message("missing.flush.problem.descriptor"))
        }

      }
    }

  }

  companion object {

    private var preferencesSubClasses: Collection<PsiClass>? = null

    private fun getPreferenceSubClasses(project: Project): Collection<PsiClass> {
      if (preferencesSubClasses == null) {
        val preferenceClass = project.findClass("com.badlogic.gdx.Preferences")
        @Suppress("LiftReturnOrAssignment")
        if (preferenceClass != null) {
          val cs = ClassInheritorsSearch.search(preferenceClass).findAll().toMutableSet()
          cs.add(preferenceClass)
          preferencesSubClasses = cs
        } else {
          preferencesSubClasses = listOf()
        }
      }

      return preferencesSubClasses ?: listOf()
    }
  }

}

private class MissingFlushInspectionMethodChecker(
        val preferencesSubClasses: Collection<PsiClass>
): KtTreeVisitorVoid() {

  var lastPreferenceChange: KtElement? = null

  override fun visitQualifiedExpression(expression: KtQualifiedExpression) {

    val (className, methodName) = expression.resolveCallToStrings() ?: return

    for (subClass in preferencesSubClasses) {
      if (subClass.getKotlinFqName()?.asString() == className) {
        if (methodName.startsWith("put") || methodName == "remove") {
          lastPreferenceChange = expression
        } else if (methodName == "flush") {
          lastPreferenceChange = null
        }
      }
    }

  }

}
