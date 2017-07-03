package com.gmail.blueboxware.libgdxplugin.inspections.gradle

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.TEST_ID_MAP
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.GroovyElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

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
class GradleTestIdsInspection: LibGDXGradleBaseInspection() {

  override fun getStaticDescription() = message("testid.html.description")

  override fun getID() = "LibGDXGradleTestId"

  override fun getDisplayName() = message("testid.name.build.gradle")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {

    if (!FileUtilRt.extensionEquals(holder.file.name, "gradle")) {
      return super.buildVisitor(holder, isOnTheFly)
    }

    return GroovyPsiElementVisitor(object : GroovyElementVisitor() {

      override fun visitLiteralExpression(literal: GrLiteral) {

        (literal.value as? String)?.let { value ->
          if (TEST_ID_MAP.containsKey(value)) {
            holder.registerProblem(literal, message("testid.problem.descriptor") + ": " + TEST_ID_MAP[value])
          }
        }

      }
    })

  }

}