package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.codeInsight.AnnotationUtil
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AnnotatedElementsSearch
import com.intellij.psi.util.PsiUtil
import org.jetbrains.kotlin.asJava.elements.KtLightField
import org.jetbrains.kotlin.asJava.elements.KtLightMember
import org.jetbrains.kotlin.asJava.elements.KtLightMethod
import org.jetbrains.kotlin.idea.util.findAnnotation
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.plainContent
import org.jetbrains.kotlin.types.expressions.ExpressionTypingUtils


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
abstract class AnnotationWrapper {
  abstract fun getValue(key: String): List<String>
}

class KtAnnotationWrapper(private val ktAnnotationEntry: KtAnnotationEntry): AnnotationWrapper() {

  override fun getValue(key: String): List<String> {
    ktAnnotationEntry.valueArguments.forEach { argument ->
      val name = argument.getArgumentName()?.asName?.identifier
      if (name == key) {
        (argument.getArgumentExpression() as? KtCallExpression)?.valueArguments?.mapNotNull {
          (it.getArgumentExpression() as? KtStringTemplateExpression)?.plainContent
        }?.let { result ->
          return result
        }
        (argument.getArgumentExpression() as? KtStringTemplateExpression)?.plainContent?.let {
          return listOf(it)
        }
      }
    }
    return listOf()
  }
}

class PsiAnnotationWrapper(private val psiAnnotation: PsiAnnotation): AnnotationWrapper() {

  override fun getValue(key: String): List<String> {
    psiAnnotation.findAttributeValue(key)?.navigationElement?.let { value ->
      (value as? PsiArrayInitializerMemberValue)?.initializers?.mapNotNull {
        (it as? PsiLiteralExpression)?.innerText()
      }?.filter { !it.isEmpty() }?.let { result ->
        return result
      }
      (value as? PsiLiteralExpression)?.innerText()?.let {
        return listOf(it)
      }
    }
    return listOf()
  }

}

private fun PsiLiteralExpression.innerText(): String {
  if (text.length > 1 && text.first() == '\"' && text.last() == '\"') {
    return text.substring(1, text.length - 1)
  } else {
    return text
  }
}

internal fun PsiMethodCallExpression.getAnnotation(annotationClass: PsiClass): AnnotationWrapper? {

  PsiUtil.deparenthesizeExpression(methodExpression.qualifierExpression)?.let { qualifierExpression ->

    ((qualifierExpression as? PsiReference)?.resolve() as? PsiVariable)?.let { variable ->
      AnnotationUtil.findAnnotation(variable, annotationClass.qualifiedName)?.let {
        return PsiAnnotationWrapper(it)
      }
    }

    (((qualifierExpression as? PsiMethodCallExpression)?.methodExpression?.resolve() as? KtLightMethod)?.kotlinOrigin as? KtProperty)?.let { ktProperty ->
      AnnotatedElementsSearch.searchElements(annotationClass, GlobalSearchScope.projectScope(project), KtLightField::class.java).forEach { member ->
        if (member.kotlinOrigin == ktProperty) {
          return ktProperty.getAnnotation(annotationClass)
        }
      }
    }

  }

  return null

}

private fun KtExpression.unwrap(): KtExpression {

  var expression: KtExpression? = this

  while (true) {
    var baseExpression = KtPsiUtil.deparenthesize(expression)
    if (baseExpression is KtBinaryExpressionWithTypeRHS) {
      baseExpression = baseExpression.left
    }
    if (baseExpression is KtPostfixExpression && ExpressionTypingUtils.isExclExclExpression(baseExpression)) {
      baseExpression = baseExpression.baseExpression
    }
    if (baseExpression == expression) {
      return baseExpression ?: this
    }
    expression = baseExpression
  }

}

internal fun KtCallExpression.getAnnotation(annotationClass: PsiClass): AnnotationWrapper? {

  (context as? KtQualifiedExpression)?.receiverExpression?.unwrap()?.let { receiverExpression ->

     val annotationTarget = (receiverExpression as? KtQualifiedExpression)?.selectorExpression?.unwrap() ?: receiverExpression

      annotationTarget.references.mapNotNull { it.resolve() }.filter { it is KtProperty || it is PsiField }.let { origins ->
        origins.forEach { origin ->
          (origin as? KtProperty)?.getAnnotation(annotationClass)?.let {
            return it
          }
        }

        AnnotatedElementsSearch.searchElements(annotationClass, GlobalSearchScope.projectScope(project), PsiMember::class.java).forEach { member ->
          if (member in origins || (member as? KtLightMember<*>)?.kotlinOrigin?.let { it in origins } == true) {
            AnnotationUtil.findAnnotation(member, annotationClass.qualifiedName)?.let {
              return PsiAnnotationWrapper(it)
            }
          }
        }
      }

  }

  return null

}

internal fun KtAnnotated.getAnnotation(annotationClass: PsiClass): AnnotationWrapper? =
        annotationClass.qualifiedName?.let { qualifiedName ->
          findAnnotation(FqName(qualifiedName))?.let { annotationEntry ->
            return KtAnnotationWrapper(annotationEntry)
          }
        }
