package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.formatter.SkinCodeStyleSettings
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinFile
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils.changeColor
import com.gmail.blueboxware.libgdxplugin.testname
import com.gmail.blueboxware.libgdxplugin.utils.COLOR_CLASS_NAME
import com.gmail.blueboxware.libgdxplugin.utils.firstParent
import com.intellij.application.options.CodeStyle
import com.intellij.openapi.command.WriteCommandAction
import java.awt.Color

/*
 * Copyright 2017 Blue Box Ware
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
@Suppress("ReplaceNotNullAssertionWithElvisReturn")
class TestSetColor : LibGDXCodeInsightFixtureTestCase() {

    fun test1() = doTest { keepColorOnOneLine(true) }

    fun test2() = doTest { keepColorOnOneLine(true) }

    fun test3() = doTest { keepColorOnOneLine(true) }

    fun test4() = doTest { keepColorOnOneLine(true) }

    fun test5() = doTest { keepColorOnOneLine(true) }

    fun test6() = doTest { keepColorOnOneLine(true) }

    fun test7() = doTest { keepColorOnOneLine(false) }

    fun test8() = doTest { keepColorOnOneLine(false) }

    fun test9() = doTest { keepColorOnOneLine(true) }

    fun test10() {
        configureByFile("10.skin")
        val colorElement = myFixture.elementAtCaret.firstParent<SkinObject>()!!
        colorElement.changeColor(Color(128, 128, 128))?.let { newObject ->
            WriteCommandAction.runWriteCommandAction(project) {
                colorElement.replace(newObject)
            }
        }
        myFixture.checkResultByFile("10.after")
    }

    fun testWithTags1() {
        addLibGDX113()
        doTest {
            keepColorOnOneLine(true)
        }
    }

    fun testWithTags2() {
        addLibGDX113()
        doTest {
            keepColorOnOneLine(true)
        }
    }

    private fun keepColorOnOneLine(yesOrNo: Boolean) {
        CodeStyle.getCustomSettings(file, SkinCodeStyleSettings::class.java).DO_NOT_WRAP_COLORS = yesOrNo
    }

    fun doTest(init: (() -> Unit)? = null) {
        configureByFile(testname() + ".skin")
        init?.invoke()
        (file as? SkinFile).let { skinFile ->
            assertNotNull(skinFile)

            val newColor =
                skinFile!!
                    .getClassSpecifications("newColor")
                    .firstOrNull()
                    ?.getResource("color")
                    ?.`object`
                    ?.asColor(true)

            assertNotNull(newColor)
            val colorObject =
                skinFile.getClassSpecifications(COLOR_CLASS_NAME).firstOrNull()?.getResource("color")?.`object`
            assertNotNull(colorObject)
            colorObject!!.changeColor(newColor!!)?.let { newObject ->
                WriteCommandAction.runWriteCommandAction(project) {
                    colorObject.replace(newObject)
                }
            }
        }
        myFixture.checkResultByFile(testname() + ".after")
    }

    override fun setUp() {
        super.setUp()

        addLibGDX()

        copyFileToProject("ColorArrayHolder.java")
    }

    override fun getBasePath() = "/filetypes/skin/setColor/"

}
