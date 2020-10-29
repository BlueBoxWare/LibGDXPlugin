package com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonArray
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.impl.GdxJsonElementImpl
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import javax.swing.Icon


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
abstract class GdxJsonArrayMixin(node: ASTNode): GdxJsonArray, GdxJsonElementImpl(node) {

  override fun getPresentation(): ItemPresentation = object: ItemPresentation {

    override fun getLocationString(): String? = null

    override fun getIcon(unused: Boolean): Icon = AllIcons.Json.Array

    override fun getPresentableText(): String = "array"

  }

}