package com.gmail.blueboxware.libgdxplugin.filetypes.properties

import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.lang.properties.psi.PropertiesFile
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.KtValueArgument
import org.jetbrains.kotlin.psi.KtValueArgumentList


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
class GDXPropertiesReferenceProvider: PsiReferenceProvider() {

  override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {

    if (!element.project.isLibGDXProject()) {
      return arrayOf()
    }

    (element.context as? PsiExpressionList)?.let { args ->
      (args.context as? PsiMethodCallExpression)?.let { methodCall ->
        if (args.expressions.firstOrNull() == element) {
          return processPsiMethodCallExpression(element, methodCall)
        }
      }
    }

    (element.context as? KtValueArgument)?.let { valueArgument ->
      (valueArgument.context as? KtValueArgumentList)?.let { valueArgumentList ->
        if (valueArgumentList.arguments.firstOrNull() == valueArgument) {
          (valueArgumentList.context as? KtCallExpression)?.let { callExpression ->
            return processKtCallExpression(element, callExpression)
          }
        }
      }
    }

    return arrayOf()

  }

  private fun processKtCallExpression(element: PsiElement, callExpression: KtCallExpression): Array<PsiReference> {

    callExpression.resolveCallToStrings()?.let { (className, methodName) ->

      if (className == I18NBUNDLE_CLASS_NAME && methodName in I18NBUNDLE_PROPERTIES_METHODS) {

        val key = (element as? KtStringTemplateExpression)?.asPlainString() ?: return arrayOf()
        val propertiesFiles = callExpression.getPropertiesFiles()

        return createReferences(key, element, propertiesFiles)

      }

    }

    return arrayOf()

  }

  private fun processPsiMethodCallExpression(
          element: PsiElement,
          methodCallExpression: PsiMethodCallExpression
  ): Array<PsiReference> {

    methodCallExpression.resolveCallToStrings()?.let { (className, methodName) ->
      if (className == I18NBUNDLE_CLASS_NAME && methodName in I18NBUNDLE_PROPERTIES_METHODS) {

        val key = (element as? PsiLiteralExpression)?.asString() ?: return arrayOf()
        val propertiesFiles = methodCallExpression.getPropertiesFiles()

        return createReferences(key, element, propertiesFiles)

      }
    }

    return arrayOf()

  }

  private fun createReferences(key: String, element: PsiElement, propertiesFiles: List<String>): Array<PsiReference> =
          if (propertiesFiles.isEmpty()) {
            arrayOf(GDXPropertyReference(key, element, null))
          } else {
            propertiesFiles.mapNotNull { propertiesFileName ->
              (element.project.getPsiFile(propertiesFileName) as? PropertiesFile)?.let { propertiesFile ->
                GDXPropertyReference(key, element, propertiesFile.resourceBundle.baseName)
              }
            }.toTypedArray()
          }

}