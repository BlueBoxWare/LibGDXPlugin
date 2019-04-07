package com.gmail.blueboxware.libgdxplugin.filetypes.json.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*
import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonParserDefinition
import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonParserDefinition.Companion.CONTAINERS
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonJobject
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonString
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.TokenType.WHITE_SPACE
import org.jetbrains.kotlin.psi.psiUtil.children


/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/master/json/src/com/intellij/json/editor/folding/JsonFoldingBuilder.java
 *
 */
class GdxJsonFoldingBuilder: FoldingBuilder, DumbAware {

  override fun isCollapsedByDefault(node: ASTNode): Boolean = false

  override fun buildFoldRegions(node: ASTNode, document: Document): Array<FoldingDescriptor> =
          collectDescriptorsRecursively(node, document, mutableListOf()).toTypedArray()

  override fun getPlaceholderText(node: ASTNode): String? {

    (node.psi as? GdxJsonJobject)?.let { jObject ->

      var candidate: GdxJsonProperty? = null
      for (property: GdxJsonProperty? in jObject.propertyList) {
        val name = property?.name ?: continue
        val value = property.value?.value
        if (value is GdxJsonString) {
          if (name == "id" || name == "name") {
            candidate = property
            break
          }
          if (candidate == null) {
            candidate = property
          }
        }
      }

      if (candidate != null) {
        return "{ \"${candidate.name}\": ${candidate.value?.text ?: ""} ... }"
      }

      return "{ ... }"

    }

    if (node.elementType == ARRAY) {

      return "[ ... ]"

    } else if (node.elementType in GdxJsonParserDefinition.COMMENTS) {

      if (node.elementType == LINE_COMMENT)
        return "//... "
      else
        return "/*...*/ "
    }

    return "..."

  }

  companion object {

    private fun collectDescriptorsRecursively(
            node: ASTNode,
            document: Document,
            descriptors: MutableList<FoldingDescriptor>
    ): Collection<FoldingDescriptor> {

      val type = node.elementType

      if (type in CONTAINERS && spansMultipleLines(node, document)) {
        descriptors.add(FoldingDescriptor(node, node.textRange))
      } else if (type == BLOCK_COMMENT) {
        descriptors.add(FoldingDescriptor(node, node.textRange))
      } else if (type == LINE_COMMENT) {
        val startOffset = node.startOffset
        val endOffset = expandLineCommentsRange(node)
        if (document.getLineNumber(startOffset) != document.getLineNumber(endOffset)) {
          descriptors.add(FoldingDescriptor(node, TextRange(startOffset, endOffset)))
        }
      }

      for (child in node.children()) {
        collectDescriptorsRecursively(child, document, descriptors)
      }

      return descriptors

    }

    private fun spansMultipleLines(node: ASTNode, document: Document): Boolean {
      val range = node.textRange
      val endOffset = range.endOffset
      return document.getLineNumber(range.startOffset) <
              if (endOffset < document.textLength)
                document.getLineNumber(endOffset)
              else
                document.lineCount - 1
    }

    private fun expandLineCommentsRange(node: ASTNode): Int {
      var lastElement = node
      while (lastElement.treeNext.elementType == LINE_COMMENT || lastElement.treeNext.elementType == WHITE_SPACE) {
        lastElement = lastElement.treeNext
      }
      return lastElement.textRange.endOffset
    }

  }
}
