package com.gmail.blueboxware.libgdxplugin.filetypes.json.formatting

import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonElementTypes.*
import com.gmail.blueboxware.libgdxplugin.filetypes.json.GdxJsonParserDefinition.Companion.CONTAINERS
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonArray
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonJobject
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.json.psi.GdxJsonValue
import com.intellij.formatting.*
import com.intellij.json.formatter.JsonCodeStyleSettings
import com.intellij.json.formatter.JsonCodeStyleSettings.ALIGN_PROPERTY_ON_COLON
import com.intellij.json.formatter.JsonCodeStyleSettings.ALIGN_PROPERTY_ON_VALUE
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet


/*
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/182.2949/json/src/com/intellij/json/formatter/JsonBlock.java
 *
 */
class GdxJsonBlock(
    private val parent: GdxJsonBlock?,
    private val node: ASTNode,
    private val customSettings: JsonCodeStyleSettings,
    private val alignment: Alignment?,
    private val indent: Indent,
    private val wrap: Wrap?,
    private val spacingBuilder: SpacingBuilder
) : ASTBlock {

    private val psiElement: PsiElement = node.psi

    private val childWrap: Wrap? = when (psiElement) {
        is GdxJsonJobject -> Wrap.createWrap(customSettings.OBJECT_WRAPPING, true)
        is GdxJsonArray -> Wrap.createWrap(customSettings.ARRAY_WRAPPING, true)
        else -> null
    }

    private val propertyValueAlignment: Alignment? = if (psiElement is GdxJsonJobject) {
        Alignment.createAlignment(true)
    } else {
        null
    }

    private var subBlocks: MutableList<Block>? = null

    override fun getNode(): ASTNode = node

    override fun getTextRange(): TextRange = node.textRange

    override fun getWrap(): Wrap? = wrap

    override fun getIndent(): Indent = indent

    override fun getAlignment(): Alignment? = alignment

    override fun isLeaf(): Boolean =
        node.firstChildNode == null

    override fun getSpacing(child1: Block?, child2: Block): Spacing? =
        spacingBuilder.getSpacing(this, child1, child2)

    override fun isIncomplete(): Boolean =
        node.lastChildNode?.let { lastChild ->
            when (node.elementType) {
                JOBJECT -> lastChild.elementType != R_CURLY
                ARRAY -> lastChild.elementType != R_BRACKET
                PROPERTY -> (node.psi as? GdxJsonProperty)?.value == null
                else -> false
            }
        } ?: false

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes =
        when {
            node.elementType in CONTAINERS -> ChildAttributes(Indent.getNormalIndent(), null)
            node.psi is PsiFile -> ChildAttributes(Indent.getNoneIndent(), null)
            else -> ChildAttributes(null, null)
        }

    override fun getSubBlocks(): List<Block> {
        if (subBlocks == null) {
            val children = node.getChildren(null)
            subBlocks = mutableListOf()
            for (child in children) {
                if (isWhiteSpaceOrEmpty(child)) {
                    continue
                }
                subBlocks?.add(makeSubBlock(child, customSettings.PROPERTY_ALIGNMENT))
            }
        }

        return subBlocks ?: listOf()
    }

    private fun makeSubBlock(childNode: ASTNode, propertyALignment: Int): Block {

        var indent = Indent.getNoneIndent()
        var alignment: Alignment? = null
        var wrap: Wrap? = null

        if (node.elementType in CONTAINERS) {

            if (childNode.elementType == COMMA) {
                wrap = Wrap.createWrap(WrapType.NONE, true)
            } else if (childNode.elementType !in BRACES) {
                wrap = childWrap
                indent = Indent.getNormalIndent()
            } else if (childNode.elementType in OPEN_BRACES) {
                if ((psiElement as? GdxJsonValue)?.isPropertyValue == true && propertyALignment == ALIGN_PROPERTY_ON_VALUE) {
                    alignment = parent?.parent?.propertyValueAlignment
                }
            }

        } else if (node.elementType == PROPERTY) {

            if (childNode.elementType == COLON && propertyALignment == ALIGN_PROPERTY_ON_COLON) {
                alignment = parent?.propertyValueAlignment
            } else if ((childNode.psi as? GdxJsonValue)?.isPropertyValue == true && propertyALignment == ALIGN_PROPERTY_ON_VALUE) {
                if (childNode.elementType !in CONTAINERS) {
                    alignment = parent?.propertyValueAlignment
                }
            }

        }

        return GdxJsonBlock(this, childNode, customSettings, alignment, indent, wrap, spacingBuilder)

    }

    companion object {

        private val OPEN_BRACES = TokenSet.create(L_CURLY, L_BRACKET)
        private val CLOSE_BRACES = TokenSet.create(R_CURLY, R_BRACKET)
        private val BRACES = TokenSet.orSet(OPEN_BRACES, CLOSE_BRACES)

        private fun isWhiteSpaceOrEmpty(node: ASTNode) =
            node.elementType == TokenType.WHITE_SPACE || node.textLength == 0

    }

}
