/*
 * Copyright 2026 Blue Box Ware
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

package com.gmail.blueboxware.libgdxplugin.aitree

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.formatting.TreeCodeStyleSettings
import com.gmail.blueboxware.libgdxplugin.filetypes.tree.inspections.TreeUnknownClassInspection
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.application.options.CodeStyle

class TestSuppressQuickfixes : LibGDXCodeInsightFixtureTestCase() {

    fun testForFile() = doTestQuickFix(TreeUnknownClassInspection::class, message("suppress.file"), "tree")

    fun testLine() = doTestQuickFix(TreeUnknownClassInspection::class, message("suppress.line"), "tree")

    fun testTreeNoSpace() {
        CodeStyle.getSettings(project).getCustomSettings(TreeCodeStyleSettings::class.java).apply {
            LINE_COMMENT_ADD_SPACE_IN_SUPPRESSION = false
        }
        doTestQuickFix(TreeUnknownClassInspection::class, message("suppress.tree"), "tree")
    }

    fun testLine1Column() {
        CodeStyle.getSettings(project).getCustomSettings(TreeCodeStyleSettings::class.java).apply {
            LINE_COMMENT_AT_FIRST_COLUMN = true
        }
        doTestQuickFix(TreeUnknownClassInspection::class, message("suppress.line"), "tree")
    }

    override fun setUp() {
        super.setUp()
        addLibGDX()
    }

    override fun getBasePath() = "filetypes/aitree/suppressQuickfixes"

}
