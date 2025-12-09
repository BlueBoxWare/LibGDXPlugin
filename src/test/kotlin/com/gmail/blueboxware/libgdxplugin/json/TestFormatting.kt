package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.intellij.application.options.CodeStyle
import com.intellij.json.JsonLanguage
import com.intellij.json.formatter.JsonCodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings


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

    fun testDefaultStyle1() = doTestFormatting("1.lson", "1.default", "lson")

    fun testDefaultStyle2() = doTestFormatting("2.lson", "2.default", "lson")

    fun testErrors() = doTestFormatting("3.lson", "3.default", "lson")

    fun testEdgeCases() = doTestFormatting("4.lson", "4.default", "lson")

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
        doTestFormatting("4.lson", "4.spaces", "lson")
    }

    fun testEdgeCasesWrap() {
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).ARRAY_WRAPPING =
            CommonCodeStyleSettings.WRAP_ALWAYS
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).OBJECT_WRAPPING =
            CommonCodeStyleSettings.WRAP_ALWAYS
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).PROPERTY_ALIGNMENT =
            JsonCodeStyleSettings.PropertyAlignment.ALIGN_ON_COLON.id
        doTestFormatting("4.lson", "4.wrap", "lson")
    }

    fun testSpacesInsideBrackets() {
        CodeStyle.getSettings(project).getCommonSettings(JsonLanguage.INSTANCE).SPACE_WITHIN_BRACES = true
        CodeStyle.getSettings(project).getCommonSettings(JsonLanguage.INSTANCE).SPACE_WITHIN_BRACKETS = true
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).ARRAY_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).OBJECT_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        doTestFormatting("1.lson", "1.spacesInsideBrackets", "lson")
    }

    fun testAlignOnColon1() {
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).PROPERTY_ALIGNMENT =
            JsonCodeStyleSettings.PropertyAlignment.ALIGN_ON_COLON.id
        doTestFormatting("1.lson", "1.alignOnColon", "lson")
    }

    fun testAlignOnColon2() {
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).PROPERTY_ALIGNMENT =
            JsonCodeStyleSettings.PropertyAlignment.ALIGN_ON_COLON.id
        doTestFormatting("2.lson", "2.alignOnColon", "lson")
    }

    fun testWrapIfLong1() {
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).ARRAY_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).OBJECT_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        doTestFormatting("1.lson", "1.wrapIfLong", "lson")
    }

    fun testWrapIfLong2() {
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).ARRAY_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        CodeStyle.getSettings(project).getCustomSettings(JsonCodeStyleSettings::class.java).OBJECT_WRAPPING =
            CommonCodeStyleSettings.WRAP_AS_NEEDED
        doTestFormatting("2.lson", "2.wrapIfLong", "lson")
    }

    override fun getBasePath() = "/filetypes/json/formatting/"

}
