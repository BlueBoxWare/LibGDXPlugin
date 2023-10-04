package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.json.LibGDXJsonFileType
import com.intellij.testFramework.LightPlatformCodeInsightTestCase


/*
 * Copyright 2021 Blue Box Ware
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
class TestEditing : LibGDXCodeInsightFixtureTestCase() {

    fun testIndentOnEnter1() = doTest(
        """
          {
          <caret>
          }
          """.trimIndent(),
        """
          {
          
          ____<caret>
          }
          """.trimIndent()
    )

    fun testIndentOnEnter2() = doTest(
        """
          {<caret>}
          """.trimIndent(),
        """
          {
          ____<caret>
          }
          """.trimIndent()
    )

    fun testIndentOnEnter3() = doTest(
        """
            {
                foo: bar<caret>
            }
          """.trimIndent(),
        """
            {
                foo: bar
            ____<caret>
            }
          """.trimIndent()
    )

    fun testCompletingObject() = doTest(
        """
            {
            foo:bar
            <caret>
          """.trimIndent(),
        """
            {
            ____foo: bar
            }
          """.trimIndent(),
        '}'
    )

    fun doTest(source: String, expected: String, key: Char = '\n') {
        myFixture.configureByText(LibGDXJsonFileType, source.replace('_', ' '))
        if (key == '\b') {
            LightPlatformCodeInsightTestCase.backspace(editor, project)
        } else {
            myFixture.type(key)
        }
        myFixture.checkResult(expected.replace('_', ' '))
    }

}
