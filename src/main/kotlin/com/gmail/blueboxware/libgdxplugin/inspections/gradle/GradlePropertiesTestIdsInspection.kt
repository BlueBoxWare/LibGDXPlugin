package com.gmail.blueboxware.libgdxplugin.inspections.gradle

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.TEST_ID_MAP
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.properties.psi.Property
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor

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
class GradlePropertiesTestIdsInspection: LibGDXGradlePropertiesBaseInspection() {

  override fun getStaticDescription() = message("testid.html.description")

  override fun getID() = "LibGDXGradlePropertiesTestId"

  override fun getDisplayName() = message("testid.name.gradle.properties")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {

    if (holder.file.name != "gradle.properties") {
      return super.buildVisitor(holder, isOnTheFly)
    }

    return object : PsiElementVisitor() {

      override fun visitElement(element: PsiElement?) {

        if (element !is Property) {
          return
        }

        element.value?.let { str ->
          for ((key, value) in TEST_ID_MAP) {
            if (str.contains(key)) {
              holder.registerProblem(element, message("testid.problem.descriptor") + ": " + value)
            }
          }
        }

      }
    }

  }

}