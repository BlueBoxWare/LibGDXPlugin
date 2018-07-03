package com.gmail.blueboxware.libgdxplugin.inspections

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.references.AssetReference
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement


/*
 * Copyright 2018 Blue Box Ware
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
internal fun checkForNonExistingAssetReference(element: PsiElement, elementName: String, holder: ProblemsHolder) {

  element
          .references
          .filterIsInstance<AssetReference>()
          .takeIf { !it.isEmpty() }
          ?.firstOrNull { it.multiResolve(true).isEmpty() }
          ?.let { reference ->

            val type = reference.className ?: "<unknown>"
            val files = reference.filesPresentableText(true).takeIf { it != "" }?.let { "in $it" } ?: ""

            holder.registerProblem(element, message("nonexisting.asset.problem.descriptor", elementName, type, files))

          }

}