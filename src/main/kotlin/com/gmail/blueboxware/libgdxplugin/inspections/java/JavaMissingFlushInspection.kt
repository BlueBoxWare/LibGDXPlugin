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
import com.gmail.blueboxware.libgdxplugin.utils.resolveCall
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*

class JavaMissingFlushInspection : LibGDXJavaBaseInspection() {

    override fun getStaticDescription() = message("missing.flush.html.description")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object : JavaElementVisitor() {

        override fun visitMethod(method: PsiMethod) {

            val methodChecker = MissingFlushInspectionMethodChecker()

            method.accept(methodChecker)

            methodChecker.lastPreferenceChange?.let { lastPreferenceChange ->
                holder.registerProblem(lastPreferenceChange, message("missing.flush.problem.descriptor"))
            }
        }
    }

    private class MissingFlushInspectionMethodChecker : JavaRecursiveElementVisitor() {

        var lastPreferenceChange: PsiExpression? = null

        override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
            super.visitMethodCallExpression(expression)

            val (receiverClass, method) = expression.resolveCall() ?: return

            var isPreferences = false

            val superTypes = receiverClass.supers.flatMap { it.supers.toList() }.toMutableList()
            superTypes.add(receiverClass)

            for (superType in superTypes) {
                if (superType.qualifiedName == "com.badlogic.gdx.Preferences") {
                    isPreferences = true
                    break
                }
            }

            if (!isPreferences) return

            if (method.name.startsWith("put") || method.name == "remove") {
                lastPreferenceChange = expression
            } else if (method.name == "flush") {
                lastPreferenceChange = null
            }

        }

    }

}

