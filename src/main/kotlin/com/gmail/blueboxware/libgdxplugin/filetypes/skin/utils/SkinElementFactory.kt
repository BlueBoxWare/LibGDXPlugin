package com.gmail.blueboxware.libgdxplugin.filetypes.skin.utils

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinLanguage
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.formatter.SkinCodeStyleSettings
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.*
import com.gmail.blueboxware.libgdxplugin.utils.childOfType
import com.gmail.blueboxware.libgdxplugin.utils.findElement
import com.gmail.blueboxware.libgdxplugin.utils.toHexString
import com.gmail.blueboxware.libgdxplugin.utils.toRGBComponents
import com.intellij.codeInspection.SuppressionUtil.createComment
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.psi.psiUtil.startOffset
import java.awt.Color

/*
 * Copyright 2016 Blue Box Ware
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
class SkinElementFactory(private val project: Project) {

  constructor(element: PsiElement): this(element.project)

  fun createSuppressionComment(inspectionId: String): PsiComment? =
          createComment(project, " @Suppress($inspectionId)", LibGDXSkinLanguage.INSTANCE)

  fun createProperty(name: String, value: String): SkinProperty? =
          createElement("""
            {
              class: {
                resource: {
                  $name: $value
                }
              }
            }
          """)

  fun createComma(): LeafPsiElement? =
          createElement("""
            {
              class: {
                resource: {
                  a: a, b: b
                }
              }
            }
          """, ',')

  fun createNewLine(): PsiWhiteSpace? =
          createElement("""
            {

            }
          """, '\n')

  fun createWhitespace(str: String): PsiWhiteSpace? =
          createElement("{$str}", 1)

  fun createLeftBrace(): LeafPsiElement? =
          createElement("{}", '{')

  fun createRightBrace(): LeafPsiElement? =
          createElement("{}", '}')

  fun createClassSpec(name: String): SkinClassSpecification? =
          createElement("""
            {
              $name: {
              }
            }
          """)

  fun createResource(name: String): Pair<SkinResource, Int>? =
          createElement<SkinResource>("""
            {
              className: {
                $name: {   }
              }
            }
          """)?.let { element ->
            element.`object`?.getOpeningBrace()?.startOffset?.let {
              Pair(element, it - element.startOffset + 2)
            }
          }

  private fun createColorResource(name: String, color: Color? = null): Pair<SkinResource, Int>? =
          createElement<SkinResource>("""
            {
              className: {
                $name: { hex: "${color?.toHexString() ?: "#"}" }
              }
            }
          """)?.let { element ->
            element.text.indexOf('#').let { index ->
              Pair(element, index + 1)
            }
          }

  private fun createColorResourceWithComponents(name: String, color: Color?): Pair<SkinResource, Int>? {
    val c = (color ?: Color.WHITE ).toRGBComponents().toMap()
    return createElement<SkinResource>("""
            {
              className: {
                $name: { r: ${c["r"]}, g: ${c["g"]}, b: ${c["b"]}, a: ${c["a"]} }
              }
            }
          """)?.let { element ->
      element.text.indexOf('r').let { index ->
        Pair(element, index + 3)
      }
    }
  }

  fun createColorResource(name: String, color: Color?, useComponents: Boolean) =
          if (useComponents) {
            createColorResourceWithComponents(name, color)
          } else {
            createColorResource(name, color)
          }

  fun createTintedDrawableResource(name: String): Pair<SkinResource, Int>? =
          createElement<SkinResource>("""
            {
              className: {
                $name: {
                  color: { hex: "#ffffff" }
                  name:${" "}
                }
              }
            }
          """.trimIndent())?.let { element ->

            Regex("""name\s*:""").find(element.text)?.range?.endInclusive?.let { end ->

              if (
                      CodeStyleSettingsManager
                              .getSettings(project)
                              .getCustomSettings(SkinCodeStyleSettings::class.java)
                              ?.SPACE_AFTER_COLON != false
              ) {
                end + 2
              } else {
                end + 1
              }
            }?.let { Pair(element, it) }

          }

  fun createObject(): SkinObject? =
          createElement(
                  """
                    {
                      className: {
                        default: { }
                      }
                    }
                  """.trimIndent()
          )

  fun createPropertyName(name: String, quote: Boolean): SkinPropertyName? =
          createElement("propertyName", name, quote)

  fun createResourceName(name: String, quote: Boolean): SkinResourceName? =
          createElement("resourceName", name, quote)

  fun createStringLiteral(value: String, quote: Boolean): SkinStringLiteral? =
          createElement<SkinPropertyValue>("propertyValue", value, quote)?.let {
            it.value as? SkinStringLiteral
          }

  inline fun <reified FIRST, reified SECOND, reified THIRD> createElementsOrNull(
          first: () -> FIRST?,
          second: () -> SECOND?,
          third: () -> THIRD?
  ): Triple<FIRST, SECOND, THIRD>? =
          first()?.let { f ->
            second()?.let { s ->
              third()?.let { t ->
                Triple(f, s, t)
              }
            }
          }

  private inline fun <reified T: PsiElement> createElement(replace: String, with: String, quote: Boolean): T? {
    val quoteChar = if (quote) "\"" else ""
    val replacement = if (quote) StringUtil.escapeStringCharacters(with) else with
    val content = DUMMY_CONTENT.replace(replace, quoteChar + replacement + quoteChar)
    return createElement(content)
  }

  private inline fun <reified T: PsiElement> createElement(content: String): T? =
          createFile(content)?.childOfType()

  private inline fun <reified T: PsiElement> createElement(content: String, noinline selector: (PsiElement) -> Boolean): T? =
          createFile(content)?.findElement(selector) as? T

  private inline fun <reified T: PsiElement> createElement(content: String, position: Int): T? =
          createFile(content)?.findElementAt(position) as? T

  private inline fun <reified T: PsiElement> createElement(content: String, character: Char): T? =
          createElement(content, content.indexOf(character))

  private fun createFile(content: String) =
          PsiFileFactory.getInstance(project)?.createFileFromText("dummy.skin", LibGDXSkinFileType.INSTANCE, content) as? SkinFile

  companion object {


    private val DUMMY_CONTENT = """
      {
          className: {
              resourceName: {
                  propertyName: propertyValue
              }
          }
      }
        """.trimIndent()

  }

}