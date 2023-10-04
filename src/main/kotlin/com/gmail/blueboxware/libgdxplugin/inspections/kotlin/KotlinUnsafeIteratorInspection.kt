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
import com.gmail.blueboxware.libgdxplugin.utils.iteratorsMap
import com.gmail.blueboxware.libgdxplugin.utils.resolveCall
import com.intellij.codeInspection.ProblemsHolder
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.psi.KtForExpression
import org.jetbrains.kotlin.psi.KtQualifiedExpression
import org.jetbrains.kotlin.psi.KtVisitorVoid
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe


internal class KotlinUnsafeIteratorInspection : LibGDXKotlinBaseInspection() {

    override fun getStaticDescription() = message("unsafeiterator.html.description")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object : KtVisitorVoid() {

        override fun visitQualifiedExpression(expression: KtQualifiedExpression) {
            super.visitQualifiedExpression(expression)

            val (receiverType, methodName) = expression.resolveCall() ?: return

            val receiverTypeFqName = receiverType.fqNameSafe.asString()
            val receiverTypeShortName = receiverType.name

            if (!iteratorsMap.containsKey(receiverTypeFqName)) return

            if (iteratorsMap[receiverTypeFqName]?.contains(methodName) != true) return

            holder.registerProblem(
                expression,
                "${message("unsafeiterator.problem.descriptor")}: $receiverTypeShortName.$methodName()"
            )

        }

        override fun visitForExpression(expression: KtForExpression) {
            super.visitForExpression(expression)

            expression.loopRange?.let { loopRange ->
                val iteratorType = expression.analyze().getType(loopRange)?.constructor?.declarationDescriptor ?: return

                val iteratorTypeFqName = iteratorType.fqNameSafe.asString()
                val iteratorTypeShortName = iteratorType.name

                if (iteratorsMap.containsKey(iteratorTypeFqName)
                    && (iteratorsMap[iteratorTypeFqName]?.contains("iterator") == true)
                ) {
                    holder.registerProblem(
                        loopRange,
                        "${message("unsafeiterator.problem.descriptor")}: $iteratorTypeShortName.iterator()"
                    )
                }

            }

        }
    }
}


