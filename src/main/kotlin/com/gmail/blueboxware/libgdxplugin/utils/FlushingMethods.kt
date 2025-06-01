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

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReference
import com.intellij.psi.search.searches.MethodReferencesSearch
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.util.Query
import org.jetbrains.kotlin.idea.base.util.projectScope
import org.jetbrains.kotlin.idea.references.KtSimpleNameReference
import org.jetbrains.kotlin.idea.references.SyntheticPropertyAccessorReference
import org.jetbrains.kotlin.psi.*

object FlushingMethodsUtils {

    private val flushingMethodsMap: Map<String, List<String>> by lazy {
        mapOf(
            "com.badlogic.gdx.graphics.g2d.Batch" to
                    listOf(
                        "disableBlending",
                        "enableBlending",
                        "end",
                        "flush",
                        "flushAndSyncTransformMatrix",
                        "setBlendFunction",
                        "setProjectionMatrix",
                        "setShader"
                    ),
            "com.badlogic.gdx.graphics.g2d.SpriteBatch" to
                    listOf(
                        "disableBlending",
                        "enableBlending",
                        "end",
                        "flush",
                        "setBlendFunction",
                        "setProjectionMatrix",
                        "setShader",
                        "setTransformMatrix"
                    ),
            "com.badlogic.gdx.graphics.g2d.CpuSpriteBatch" to
                    listOf(
                        "disableBlending",
                        "enableBlending",
                        "end",
                        "flush",
                        "flushAndSyncTransformMatrix",
                        "setBlendFunction",
                        "setProjectionMatrix",
                        "setShader"
                    ),
            "com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch" to
                    listOf(
                        "disableBlending",
                        "enableBlending",
                        "end",
                        "flush",
                        "setBlendFunction",
                        "setProjectionMatrix",
                        "setShader",
                        "setTransformMatrix"
                    ),
            "com.badlogic.gdx.graphics.glutils.ShapeRenderer" to
                    listOf(
                        "end",
                        "flush",
                        "updateMatrices",
                        "setProjectionMatrix",
                        "setTransformMatrix",
                        "identity",
                        "translate",
                        "rotate",
                        "scale",
                        "set"
                    ),
            "com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20" to
                    listOf(
                        "end",
                        "flush"
                    ),
            "com.badlogic.gdx.graphics.g3d.decals.DecalBatch" to
                    listOf(
                        "flush"
                    ),
            "com.badlogic.gdx.graphics.g3d.ModelBatch" to
                    listOf(
                        "flush",
                        "end",
                        "setCamera"
                    )
        )
    }

    private fun getFlushingMethods(project: Project): Set<PsiMethod> {

        val result = mutableSetOf<PsiMethod>()

        for ((className, methodNames) in flushingMethodsMap) {
            val clazz = project.findClass(className) ?: continue
            for (methodName in methodNames) {
                val methods = clazz.findMethodsByName(methodName, false)
                result.addAll(methods)
            }
        }

        return result

    }

    fun getAllFlushingMethods(project: Project): Pair<Set<PsiElement>, Set<PsiElement>> {

        val result = mutableSetOf<PsiElement>()
        val queue = mutableSetOf<PsiElement>()
        val seeds = getFlushingMethods(project)

        val scope = project.projectScope()

        queue.addAll(seeds)

        while (queue.isNotEmpty()) {

            val method = queue.first()
            result.add(method)

            var refs: Query<PsiReference>?

            if (method is PsiMethod) {
                refs = MethodReferencesSearch.search(method, scope, true)
            } else {
                refs = ReferencesSearch.search(method, scope)

                (method.context as? KtProperty)?.let {
                    queue.add(it)
                }
            }

            refs.let { refsNotNull ->
                for (ref in refsNotNull.findAll()) {
                    if (!ref.isReferenceTo(method)) {
                        continue // Workaround for https://youtrack.jetbrains.com/issue/IDEA-90019
                    }
                    if (
                        ref.element.context is PsiMethodCallExpression
                        || ref.element.context is KtProperty
                        || ref.element.context is KtCallExpression
                        || ref is KtSimpleNameReference
                        || ref is SyntheticPropertyAccessorReference
                    ) {
                        val functionalParent = getFunctionalParent(ref.element)
                        functionalParent?.let { parent ->
                            if (!result.contains(parent)) {
                                queue.add(parent)
                            }
                        }
                    }
                }
            }

            queue.remove(method)

        }

        return Pair(seeds, result)

    }

    private fun getFunctionalParent(element: PsiElement): PsiElement? {

        var parent = element.parent

        while (parent != null) {
            if (parent is PsiMethod || parent is KtFunction || parent is KtClassInitializer || parent is KtProperty || parent is KtPropertyAccessor) {
                return parent
            }
            parent = parent.parent
        }

        return null

    }

}
