package com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinElement
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.*
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement

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
abstract class SkinBaseInspection: LocalInspectionTool() {

  init {
    if (ApplicationManager.getApplication().isUnitTestMode) {
      assert(INSPECTION_NAMES.contains(id))
    }
  }

  override fun getGroupPath() = arrayOf("libGDX", "Skin files")

  @Suppress("DialogTitleCapitalization")
  override fun getGroupDisplayName() = "libGDX"

  override fun isEnabledByDefault() = true

  override fun isSuppressedFor(element: PsiElement): Boolean =
          (element as? SkinElement)?.isSuppressed(id) ?: false

  override fun getBatchSuppressActions(element: PsiElement?): Array<SuppressQuickFix> =
          arrayOf(
                  SuppressForPropertyFix(id),
                  SuppressForObjectFix(id),
                  SuppressForFileFix(id)
          )

}