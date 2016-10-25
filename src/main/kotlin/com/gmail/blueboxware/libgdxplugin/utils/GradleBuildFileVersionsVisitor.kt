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
package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.psi.PsiElement
import org.jetbrains.plugins.groovy.lang.psi.GroovyRecursiveElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral
import org.jetbrains.plugins.groovy.lang.psi.impl.statements.expressions.literals.GrLiteralImpl

abstract class GradleBuildFileVersionsVisitor: GroovyRecursiveElementVisitor() {

  abstract fun onVersionFound(library: GDXLibrary, version: String, element: PsiElement)

  override fun visitLiteralExpression(literal: GrLiteral?) {
    super.visitLiteralExpression(literal)

    if (literal == null || literal !is GrLiteralImpl || !literal.isStringLiteral) return

    val (lib, version) = extractInfoFromMavenCoord(literal.text) ?: return

    onVersionFound(lib, version, literal)

  }

  override fun visitAssignmentExpression(expression: GrAssignmentExpression?) {
    super.visitAssignmentExpression(expression)

    if (expression == null) return
    expression.rValue?.let { rValue ->

      val key = expression.lValue.text
      val value = if (rValue is GrLiteral) rValue.value else return

      val lib = gradleExtNameMap[key] ?: return

      onVersionFound(lib, value.toString(), expression)
    }

  }

}