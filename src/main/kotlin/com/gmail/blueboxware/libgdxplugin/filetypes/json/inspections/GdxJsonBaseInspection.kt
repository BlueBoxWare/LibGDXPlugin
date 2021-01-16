package com.gmail.blueboxware.libgdxplugin.filetypes.json.inspections

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonElement
import com.gmail.blueboxware.libgdxplugin.filetypes.json.utils.isSuppressed
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement


/*
 * Copyright 2019 Blue Box Ware
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
abstract class GdxJsonBaseInspection: LocalInspectionTool() {

  protected fun getShortID() = id.removePrefix("LibGDXJson")

  override fun getGroupPath() = arrayOf("libGDX", "JSON")

  @Suppress("DialogTitleCapitalization")
  override fun getGroupDisplayName() = "libGDX"

  override fun isEnabledByDefault() = true

  override fun isSuppressedFor(element: PsiElement): Boolean =
          (element as? GdxJsonElement)?.isSuppressed(getShortID()) ?: false

  abstract override fun getBatchSuppressActions(element: PsiElement?): Array<SuppressQuickFix>


}