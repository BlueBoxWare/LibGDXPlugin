/*
 * Copyright 2025 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.filetypes.tree.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.util.NlsContexts
import icons.Icons
import javax.swing.Icon

internal class TreeColorSettingsPage : ColorSettingsPage {

    override fun getIcon(): Icon = Icons.GDXAI_TREE_FILETYPE

    override fun getHighlighter(): SyntaxHighlighter = TreeSyntaxHighlighter()

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String?, TextAttributesKey?> = mapOf(
        "attrname" to TreeSyntaxHighlighter.ATTRNAME,
        "subtree" to TreeSyntaxHighlighter.SUBTREEREF,
        "taskname" to TreeSyntaxHighlighter.TASKNAME
    )

    override fun getColorDescriptors(): Array<out ColorDescriptor?> = emptyArray()

    @Suppress("DialogTitleCapitalization")
    override fun getDisplayName(): @NlsContexts.ConfigurableName String = "libGDX AI Tree"

    override fun getDemoText(): String = """
# Comment
import <attrname>bark</attrname>:"com.badlogic.gdx.ai.tests.btree.dog.BarkTask"

subtree <attrname>name</attrname>:"mytree"

root
  <taskname>selector</taskname>
    <taskname>parallel</taskname>
      <taskname>care</taskname>? <attrname>urgentProb</attrname>?:0.8
      (<subtree>${'$'}mytree</subtree>) <taskname>alwaysFail</taskname>
        <taskname>com.badlogic.gdx.ai.tests.btree.dog.RestTask</taskname> # comment
    <taskname>sequence</taskname>
      <taskname>bark</taskname> <attrname>times</attrname>:"uniform,1,3"
      <taskname>walk</taskname> <attrname>boolean</attrname>:true <attrname>null</attrname>:null <attrname>number</attrname>:1.337 <attrname>string</attrname>:"string"
      <taskname>com.badlogic.gdx.ai.tests.btree.dog.BarkTask</taskname>
      (<taskname>guard</taskname> <attrname>foo</attrname>:"bar") <taskname>mark</taskname>
    """.trimIndent()

    private fun a(name: String, key: TextAttributesKey) = AttributesDescriptor(name, key)

    override fun getAttributeDescriptors(): Array<out AttributesDescriptor?> = arrayOf(
        a("Values//Boolean", TreeSyntaxHighlighter.BOOL),
        a("Values//Number", TreeSyntaxHighlighter.NUMBER),
        a("Values//String", TreeSyntaxHighlighter.STRING),
        a("Values//Null", TreeSyntaxHighlighter.NULL),

        a("Characters//Colon", TreeSyntaxHighlighter.COLON),
        a("Characters//Parentheses", TreeSyntaxHighlighter.PAREN),
        a("Characters//Question mark", TreeSyntaxHighlighter.QUESTION_MARK),

        a("Task name", TreeSyntaxHighlighter.TASKNAME),
        a("Attribute name", TreeSyntaxHighlighter.ATTRNAME),
        a("Subtree reference", TreeSyntaxHighlighter.SUBTREEREF),

        a("Special task name//Import", TreeSyntaxHighlighter.IMPORT),
        a("Special task name//Root", TreeSyntaxHighlighter.ROOT),
        a("Special task name//Subtree", TreeSyntaxHighlighter.SUBTREE),

        a("Comment", TreeSyntaxHighlighter.COMMENT)
    )

}
