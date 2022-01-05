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
package com.gmail.blueboxware.libgdxplugin.inspections.gradle

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.plugins.groovy.lang.psi.GroovyElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementVisitor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrCommandArgumentList
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.literals.GrLiteral

class GradleOutdatedVersionsInspection : LibGDXGradleBaseInspection() {

    override fun getStaticDescription() =
        message("outdated.version.inspection.static.description", Libraries.listOfCheckedLibraries())

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) =
        GroovyPsiElementVisitor(object : GroovyElementVisitor() {

            override fun visitLiteralExpression(literal: GrLiteral) {

                if (!literal.isInGradleBuildFile()) {
                    return
                }

                getLibraryInfoFromGroovyLiteral(literal)?.let { (lib, version) ->
                    checkVersionAndReport(holder, literal, lib, version)
                }

            }

            override fun visitCommandArguments(argumentList: GrCommandArgumentList) {

                if (!argumentList.isInGradleBuildFile()) {
                    return
                }

                getLibraryInfoFromGroovyArgumentList(argumentList)?.let { (lib, version) ->
                    checkVersionAndReport(holder, argumentList, lib, version)
                }

            }

            override fun visitAssignmentExpression(expression: GrAssignmentExpression) {

                getLibraryInfoFromGroovyAssignment(expression)?.let { (lib, version) ->
                    checkVersionAndReport(holder, expression, lib, version)
                }

            }

        })

}
