package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.intellij.application.options.CodeStyle
import com.intellij.formatting.FormatterTestUtils
import com.intellij.json.JsonLanguage
import com.intellij.json.formatter.JsonCodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import java.io.File


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
class TestFormatting : LibGDXCodeInsightFixtureTestCase() {

    fun testDefaultStyle1() = doFileTest("1.lson", "1.default")

    fun testDefaultStyle2() = doFileTest("2.lson", "2.default")

    fun testErrors() = doFileTest("3.lson", "3.default")

    fun testEdgeCases() = doFileTest("4.lson", "4.default")

    fun testEdgeCasesAllSpaces() {
        CodeStyle.getSettings(project).getCommonSettings(JsonLanguage.INSTANCE).apply {
            SPACE_WITHIN_BRACES = true
            SPACE_WITHIN_BRACKETS = true
            SPACE_AFTER_COLON = true
            SPACE_AFTER_COMMA = true
            SPACE_BEFORE_COLON = true
            SPACE_BEFORE_COMMA = true
        }
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).ARRAY_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).OBJECT_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        doFileTest("4.lson", "4.spaces")
    }

    fun testEdgeCasesWrap() {
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).ARRAY_WRAPPING =
            CommonCodeStyleSettings.WRAP_ALWAYS
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).OBJECT_WRAPPING =
            CommonCodeStyleSettings.WRAP_ALWAYS
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).PROPERTY_ALIGNMENT =
            JsonCodeStyleSettings.PropertyAlignment.ALIGN_ON_COLON.id
        doFileTest("4.lson", "4.wrap")
    }

    fun testSpacesInsideBrackets() {
        CodeStyle.getSettings(project).getCommonSettings(JsonLanguage.INSTANCE).SPACE_WITHIN_BRACES = true
        CodeStyle.getSettings(project).getCommonSettings(JsonLanguage.INSTANCE).SPACE_WITHIN_BRACKETS = true
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).ARRAY_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).OBJECT_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        doFileTest("1.lson", "1.spacesInsideBrackets")
    }

    fun testAlignOnColon1() {
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).PROPERTY_ALIGNMENT =
            JsonCodeStyleSettings.PropertyAlignment.ALIGN_ON_COLON.id
        doFileTest("1.lson", "1.alignOnColon")
    }

    fun testAlignOnColon2() {
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).PROPERTY_ALIGNMENT =
            JsonCodeStyleSettings.PropertyAlignment.ALIGN_ON_COLON.id
        doFileTest("2.lson", "2.alignOnColon")
    }

    fun testWrapIfLong1() {
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).ARRAY_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).OBJECT_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        doFileTest("1.lson", "1.wrapIfLong")
    }

    fun testWrapIfLong2() {
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).ARRAY_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).OBJECT_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        doFileTest("2.lson", "2.wrapIfLong")
    }

    private fun doFileTest(beforeFile: String, afterFile: String) =
        FormatterTestUtils.testFormatting(
            project,
            "lson",
            File(testDataPath, beforeFile).readText(),
            File(testDataPath, afterFile).readText(),
            FormatterTestUtils.Action.REFORMAT
        )

    override fun getBasePath() = "/filetypes/json/formatting/"

}
