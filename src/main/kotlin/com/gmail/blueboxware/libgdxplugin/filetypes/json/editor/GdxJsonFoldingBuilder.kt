package com.gmail.blueboxware.libgdxplugin.filetypes.json.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.json.COMMENTS
import com.gmail.blueboxware.libgdxplugin.filetypes.json.CONTAINERS
import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonJobject
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonLiteral
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonProperty
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType.WHITE_SPACE
import org.jetbrains.kotlin.psi.psiUtil.children


/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/master/json/src/com/intellij/json/editor/folding/JsonFoldingBuilder.java
 *
 */
internal class GdxJsonFoldingBuilder : FoldingBuilder, DumbAware {

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false

    override fun buildFoldRegions(node: ASTNode, document: Document): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()
        collectDescriptorsRecursively(node, document, descriptors)
        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String {

        (node.psi as? GdxJsonJobject)?.let { jObject ->

            var candidate: GdxJsonProperty? = null
            for (property: GdxJsonProperty? in jObject.propertyList) {
                val name = property?.name ?: continue
                val value = property.value?.value
                if (value is GdxJsonLiteral) {
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

        } else if (node.elementType in COMMENTS) {

            return if (node.elementType == LINE_COMMENT)
                "//... "
            else
                "/*...*/ "
        }

        return "..."

    }

}

private fun expandLineCommentsRange(element: PsiElement): Pair<Int, Int> =
    Pair(
        findLineComment(element, false).textRange.startOffset,
        findLineComment(element, true).textRange.endOffset
    )

private fun collectDescriptorsRecursively(
    node: ASTNode,
    document: Document,
    descriptors: MutableList<FoldingDescriptor>
) {

    val type = node.elementType

    if (type in CONTAINERS && spansMultipleLines(node, document)) {
        descriptors.add(FoldingDescriptor(node, node.textRange))
    } else if (type == BLOCK_COMMENT) {
        descriptors.add(FoldingDescriptor(node, node.textRange))
    } else if (type == LINE_COMMENT) {
        expandLineCommentsRange(node.psi).let { (start, end) ->
            if (document.getLineNumber(start) != document.getLineNumber(end)) {
                descriptors.add(FoldingDescriptor(node, TextRange(start, end)))
            }
        }
    }

    for (child in node.children()) {
        collectDescriptorsRecursively(child, document, descriptors)
    }

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

private fun findLineComment(element: PsiElement, after: Boolean): PsiElement {
    var node = element.node
    var lastSeen = node
    while (node != null) {
        if (node.elementType == LINE_COMMENT) {
            lastSeen = node
        } else if (node.elementType == WHITE_SPACE) {
            if (node.text.indexOf('\n', 1) != -1) {
                break
            }
        } else if (node.elementType != BLOCK_COMMENT) {
            break
        }
        node = if (after) node.treeNext else node.treePrev
    }
    return lastSeen.psi
}
