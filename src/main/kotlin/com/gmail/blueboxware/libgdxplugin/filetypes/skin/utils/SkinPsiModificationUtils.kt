package com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinFileImpl
import com.gmail.blueboxware.libgdxplugin.utils.findParentWhichIsChildOf
import com.gmail.blueboxware.libgdxplugin.utils.isNewline
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.psiUtil.allChildren
import org.jetbrains.kotlin.psi.psiUtil.nextLeaf


/*
 * Copyright 2018 Blue Box Ware
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
fun SkinElement.factory() = (containingFile as? SkinFileImpl)?.factory

fun SkinObject.addCommentExt(comment: PsiComment) {

  if (firstChild?.text == "{") {

    PsiTreeUtil.nextLeaf(firstChild)?.node?.let { nextNode ->
      TreeUtil.skipWhitespaceAndComments(nextNode, true)?.let { anchor ->
        factory()?.createNewLine()?.let { newLine ->
          addAfter(newLine, addBefore(comment, anchor.psi.findParentWhichIsChildOf(this)))
        }
      }
    }

  }

}

fun SkinClassSpecification.addCommentExt(comment: PsiComment) {

  var leaf: PsiElement? = firstChild

  while (leaf != null && leaf.node?.elementType != SkinElementTypes.L_CURLY) {
    leaf = leaf.nextLeaf()
  }

  leaf?.nextLeaf()?.node?.let { curlyNode ->

    TreeUtil.skipWhitespaceAndComments(curlyNode, true)?.let { anchor ->
      factory()?.createNewLine()?.let { newLine ->
        addAfter(newLine, addBefore(comment, anchor.psi.findParentWhichIsChildOf(this)))
      }
    }

  }

}

fun SkinClassSpecification.addResource(name: String): SkinResource? =
          factory()?.createResource(name)?.let { resource ->
            addResource(resource)
          }

fun SkinClassSpecification.addResource(resource: SkinResource): SkinResource? =
        resources?.let { resources ->
          resources.allChildren.lastOrNull()?.let { lastChild ->
            if (!lastChild.isNewline()) {
              factory()?.createNewLine()?.let {
                resources.add(it)
              }
            }
          }
          resources.add(resource) as? SkinResource
        }

fun SkinObject.addPropertyExt(property: SkinProperty) {

  if (firstChild?.text == "{") {

    addAfter(property, firstChild)?.let {
      if (propertyList.size > 1) {
        factory()?.createComma()?.let { comma ->
          addAfter(comma, it)
        }
      }
    }

  }

}