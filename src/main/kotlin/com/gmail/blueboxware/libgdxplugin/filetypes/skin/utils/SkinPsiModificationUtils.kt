package com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.SkinElementTypes
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinFileImpl
import com.gmail.blueboxware.libgdxplugin.utils.*
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.psiUtil.allChildren
import org.jetbrains.kotlin.psi.psiUtil.nextLeaf
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import java.awt.Color


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

fun SkinObject.removeAllProperties() =
        allChildren.filter { it is SkinProperty || it.isLeaf(SkinElementTypes.COMMA) }.toList().forEach {
          removeChild(it)
        }


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

fun SkinClassSpecification.addResource(name: String, cause: SkinElement? = null): Pair<SkinResource, Int>? =
          factory()?.createResource(name)?.let { (resource, position) ->
            addResource(resource, cause)?.let {
              Pair(it, it.startOffset + position)
            }
          }

fun SkinClassSpecification.addResource(resource: SkinResource, cause: SkinElement? = null): SkinResource? {

  val resources = resources ?: return null

  val addBefore = cause?.firstParent<SkinResource>()?.let { targetResource ->
    resourcesAsList.firstOrNull { it == targetResource }
  }

  val result = if (addBefore == null) {
    resources.add(resource) as? SkinResource
  } else {
    resources.addBefore(resource, addBefore) as? SkinResource
  }

  result?.let { actualResult ->
    if (!actualResult.isPrecededByNewline()) {
      factory()?.createNewLine()?.let { newline ->
        resources.addBefore(newline, actualResult)
      }
    }
    if (!actualResult.isFollowByNewLine()) {
      factory()?.createNewLine()?.let { newline ->
        resources.addAfter(newline, actualResult)
      }
    }
  }

  return result
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

fun SkinObject.changeColor(color: Color): SkinObject? {

  val newObject = factory()?.createObject() ?: return null

  if (propertyNames.contains("hex") || (propertyNames.none { listOf("r", "g", "b", "a").contains(it) })) {

    var quotationChar = "\""

    (propertyList.find { it.name == "hex" }?.propertyValue?.value as? SkinStringLiteral)?.let { oldValue ->
      quotationChar = if (oldValue.isQuoted) "\"" else ""
    }

    factory()?.createProperty("hex", quotationChar + color.toHexString() + quotationChar)?.let(newObject::addProperty)

  } else {

    val components = color.getRGBComponents(null)

    for (rgb in listOf("r", "g", "b", "a")) {

      val value = when (rgb) {
        "r" -> components[0]
        "g" -> components[1]
        "b" -> components[2]
        else -> components[3]
      }
      factory()?.createProperty(rgb, value.toString())?.let(newObject::addProperty)

    }

  }

  return newObject

}

fun SkinObject.setColor(color: Color, useHex: Boolean) {

  if (useHex) {

    factory()?.createProperty("hex", "\"" + color.toHexString() + "\"")?.let { property ->
      removeAllProperties()
      addProperty(property)
    }

  } else {

    val propertiesToAdd = mutableListOf<SkinProperty>()

    color.toRGBComponents().reversed().forEach { (component, value) ->
      factory()?.createProperty(component, value.toString())?.let {
        propertiesToAdd.add(it)
      } ?: return
    }

    removeAllProperties()

    propertiesToAdd.forEach { property ->
      addProperty(property)
    }

  }

}