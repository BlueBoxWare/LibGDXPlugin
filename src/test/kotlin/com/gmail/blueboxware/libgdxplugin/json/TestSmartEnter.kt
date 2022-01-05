package com.gmail.blueboxware.libgdxplugin.json

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.json.LibGDXJsonLanuage
import com.gmail.blueboxware.libgdxplugin.testname
import com.intellij.codeInsight.editorActions.smartEnter.SmartEnterProcessors
import com.intellij.openapi.command.WriteCommandAction


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
class TestSmartEnter : LibGDXCodeInsightFixtureTestCase() {

    fun testCommaAfterProperty() {
        doTest()
    }

    fun testCommaAfterPropertyInWord() {
        doTest()
    }

    fun testCommaAfterPropertyArray() {
        doTest()
    }

    fun testColonAfterPropertyName() {
        doTest()
    }

    fun testColonAfterPropertyNameQuotes() {
        doTest()
    }

    fun testCommaAfterArrayElement() {
        doTest()
    }

    fun testCommafterPropertyWithMultilineValue() {
        doTest()
    }

    private fun doTest() {
        configureByFileAsGdxJson(testname() + ".json")

        val processors = SmartEnterProcessors.INSTANCE.forKey(LibGDXJsonLanuage.INSTANCE)

        WriteCommandAction.runWriteCommandAction(project) {

            for (processor in processors) {
                processor.process(project, editor, file)
            }

        }

        myFixture.checkResultByFile(testname() + ".after", false)

    }

    override fun getBasePath() = "/filetypes/json/smartEnter/"

}
