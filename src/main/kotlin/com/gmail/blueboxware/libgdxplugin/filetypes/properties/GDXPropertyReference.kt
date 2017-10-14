package com.gmail.blueboxware.libgdxplugin.filetypes.properties

import com.intellij.lang.properties.BundleNameEvaluator
import com.intellij.lang.properties.PropertiesReferenceManager
import com.intellij.lang.properties.ResourceBundle
import com.intellij.lang.properties.psi.PropertiesFile
import com.intellij.lang.properties.psi.Property
import com.intellij.lang.properties.references.PropertyReferenceBase
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope


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
class GDXPropertyReference(key: String, element: PsiElement, private val bundleName: String?): PropertyReferenceBase(key, bundleName == null, element, ElementManipulators.getValueTextRange(element)) {

  override fun resolve(): PsiElement? {
    val results = multiResolve(false)

    results.firstOrNull()?.element?.getResourceBundle()?.let { firstResourceBundle ->
      if (results.all { it.element?.getResourceBundle() == firstResourceBundle }) {
        return results.first { (it.element as? Property)?.propertiesFile == firstResourceBundle.defaultPropertiesFile }.element
      }
    }

    return null
  }

  override fun getPropertiesFiles(): MutableList<PropertiesFile>? {
    if (bundleName == null) {
      return null
    }
    return retrievePropertyFilesByBundleName()
  }

  private fun retrievePropertyFilesByBundleName(): MutableList<PropertiesFile> {
    bundleName?.let { bundleName ->
      element.project.let { project ->
        PropertiesReferenceManager.getInstance(project)?.let { refManager ->
          return refManager.findPropertiesFiles(GlobalSearchScope.allScope(project), bundleName, BundleNameEvaluator.DEFAULT)
        }
      }
    }

    return mutableListOf()
  }


  private fun PsiElement.getResourceBundle(): ResourceBundle? = (this as? Property)?.propertiesFile?.resourceBundle

}