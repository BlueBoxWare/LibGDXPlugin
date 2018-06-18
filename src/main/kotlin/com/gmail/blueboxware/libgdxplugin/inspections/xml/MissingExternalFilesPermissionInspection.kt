package com.gmail.blueboxware.libgdxplugin.inspections.xml

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.androidManifest.ManifestModel
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.find.findUsages.JavaFindUsagesHelper
import com.intellij.find.findUsages.JavaMethodFindUsagesOptions
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlFile
import org.jetbrains.kotlin.idea.refactoring.fqName.getKotlinFqName
import org.jetbrains.kotlin.idea.references.KtSimpleNameReference
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.resolve.calls.callUtil.getCalleeExpressionIfAny

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
class MissingExternalFilesPermissionInspection : LibGDXXmlBaseInspection() {

  override fun getStaticDescription() = message("missing.files.permissions.html.desciption")

  override fun getID() = "LibGDXMissingFilesPermission"

  override fun getDisplayName() = message("missing.files.permissions.display.name")

  override fun getGroupPath() = arrayOf("LibGDX", "Android")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {

    if (holder.file.name != "AndroidManifest.xml") {
      return super.buildVisitor(holder, isOnTheFly)
    }

    return object : XmlElementVisitor() {

      override fun visitXmlFile(file: XmlFile?) {
        if (file == null) return

        if (ManifestModel.fromFile(file).permissions.any { it.value == "android.permission.WRITE_EXTERNAL_STORAGE" }) {
          return
        }

        val module = ModuleUtilCore.findModuleForPsiElement(file) ?: return
        val moduleWithDepsScope = GlobalSearchScope.moduleWithDependenciesScope(module)
        val moduleWithDepsAndLibsScope = GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)
        val psiFacade = JavaPsiFacade.getInstance(file.project) ?: return

        val externalFilesMethods = mutableListOf<PsiMethod>()

        // com.badlogic.gdx.Files
        psiFacade.findClass("com.badlogic.gdx.Files", moduleWithDepsAndLibsScope)?.let { clazz ->
          listOf("absolute", "external", "getFileHandle").forEach {
            externalFilesMethods.addAll(clazz.findMethodsByName(it, false))
          }
        }

        // com.badlogic.gdx.files.FileHandle
        psiFacade.findClass("com.badlogic.gdx.files.FileHandle", moduleWithDepsAndLibsScope)?.let { clazz ->
          clazz.constructors.forEach { constructor ->
            if (constructor.parameterList.parametersCount == 1) {
              externalFilesMethods.add(constructor)
            }
          }
        }

        // find method usages
        var found = false
        externalFilesMethods.forEach { method ->
          JavaFindUsagesHelper.processElementUsages(method, JavaMethodFindUsagesOptions(moduleWithDepsScope)) { usage ->

            PsiTreeUtil.findFirstParent(usage.element) { it is KtCallExpression || it is PsiMethodCallExpression || it is PsiNewExpression }?.let { callExpression ->

              val methodName = (callExpression as? KtCallExpression)?.calleeExpression?.text ?: (usage.reference?.resolve() as? PsiMethod)?.name

              if (methodName == "getFileHandle") {
                ((callExpression as? PsiMethodCallExpression)?.argumentList?.expressions?.getOrNull(1)?.reference?.resolve() as? PsiEnumConstant)?.getKotlinFqName()?.asString()?.let { fqName ->
                  if (fqName == "com.badlogic.gdx.Files.FileType.External" || fqName == "com.badlogic.gdx.Files.FileType.Absolute") {
                    found = true
                    return@processElementUsages false
                  }
                }
                ((callExpression as? KtCallExpression)
                        ?.valueArguments
                        ?.getOrNull(1)
                        ?.getArgumentExpression()
                        ?.getCalleeExpressionIfAny()
                        ?.references
                        ?.firstOrNull { it is KtSimpleNameReference }
                        ?.resolve() as? PsiEnumConstant
                        )?.getKotlinFqName()?.asString()?.let { fqName ->
                  if (fqName == "com.badlogic.gdx.Files.FileType.External" || fqName == "com.badlogic.gdx.Files.FileType.Absolute") {
                    found = true
                    return@processElementUsages false
                  }
                }
              } else {
                found = true
                return@processElementUsages false
              }

            }

            return@processElementUsages true
          }
        }

        if (found) {
          holder.registerProblem(file, message("missing.files.permissions.problem.descriptor"))
        }
      }

    }
  }

}