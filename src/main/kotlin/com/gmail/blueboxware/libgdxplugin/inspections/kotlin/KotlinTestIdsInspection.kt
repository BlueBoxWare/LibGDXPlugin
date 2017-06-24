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
import com.gmail.blueboxware.libgdxplugin.utils.TEST_ID_MAP
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.kotlin.psi.KtLiteralStringTemplateEntry
import org.jetbrains.kotlin.psi.KtVisitorVoid

class KotlinTestIdsInspection: LibGDXKotlinBaseInspection() {

  override fun getStaticDescription() = message("testid.html.description")

  override fun getID() = "LibGDXKotlinTestId"

  override fun getDisplayName() = message("testid.name")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: KtVisitorVoid() {

    override fun visitLiteralStringTemplateEntry(entry: KtLiteralStringTemplateEntry) {

      entry.text.trim().let { value ->
          if (TEST_ID_MAP.containsKey(value)) {
            holder.registerProblem(entry, message("testid.problem.descriptor") + ": " + TEST_ID_MAP[value])
          }
      }

    }

  }

}
