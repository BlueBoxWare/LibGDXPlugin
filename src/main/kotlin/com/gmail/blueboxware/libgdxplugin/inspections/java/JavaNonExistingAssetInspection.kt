package com.gmail.blueboxware.libgdxplugin.inspections.java

import com.gmail.blueboxware.libgdxplugin.inspections.checkForNonExistingAssetReference
import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.asString
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiLiteralExpression


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
class JavaNonExistingAssetInspection: LibGDXJavaBaseInspection() {

  override fun getStaticDescription() = message("nonexisting.asset.inspection.html.description")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: JavaElementVisitor() {

    override fun visitLiteralExpression(expression: PsiLiteralExpression?) {

      if (expression == null || expression.value !is String) {
        return
      }

      checkForNonExistingAssetReference(expression, expression.asString() ?: "<unknown>", holder)

    }
  }

}