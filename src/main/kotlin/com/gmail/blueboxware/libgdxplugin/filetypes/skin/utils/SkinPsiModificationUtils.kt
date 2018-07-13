package com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinClassSpecification
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinProperty
import com.gmail.blueboxware.libgdxplugin.utils.findParentWhichIsChildOf
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.util.PsiTreeUtil
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
fun SkinObject.addCommentExt(comment: PsiComment) {

  if (firstChild?.text == "{") {

    PsiTreeUtil.nextLeaf(firstChild)?.node?.let { nextNode ->
      TreeUtil.skipWhitespaceAndComments(nextNode, true)?.let { anchor ->
        SkinElementFactory(project).createNewLine()?.let { newLine ->
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
      SkinElementFactory(project).createNewLine()?.let { newLine ->
        addAfter(newLine, addBefore(comment, anchor.psi.findParentWhichIsChildOf(this)))
      }
    }

  }

}

fun SkinFile.addCommentExt(comment: PsiComment) {

  firstChild?.node?.let { firstNode ->

    TreeUtil.skipWhitespaceAndComments(firstNode, true)?.let { anchor ->
      SkinElementFactory(project).createNewLine()?.let { newLine ->
        addAfter(newLine, addBefore(comment, anchor.psi.findParentWhichIsChildOf(this)))
      }
    }

  }

}

fun SkinObject.addPropertyExt(property: SkinProperty) {

  if (firstChild?.text == "{") {

    addAfter(property, firstChild)?.let {
      if (propertyList.size > 1) {
        SkinElementFactory(project).createComma()?.let { comma ->
          addAfter(comma, it)
        }
      }
    }

  }

}