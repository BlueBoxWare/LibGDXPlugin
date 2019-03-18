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
package com.gmail.blueboxware.libgdxplugin.inspections.xml

import com.gmail.blueboxware.libgdxplugin.message
import com.gmail.blueboxware.libgdxplugin.utils.androidManifest.ManifestModel
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlFile

class OpenGLESDirectiveInspection: LibGDXXmlBaseInspection() {

  override fun getStaticDescription() = message("no.opengl.html.description")

  override fun getID() = "LibGDXOpenGLVersion"

  override fun getDisplayName() = message("no.opengl.directive.display.name")

  override fun getGroupPath() = arrayOf("LibGDX", "Android")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {

    if (holder.file.name != "AndroidManifest.xml") {
      return super.buildVisitor(holder, isOnTheFly)
    }

    return object: XmlElementVisitor() {

      override fun visitXmlFile(file: XmlFile?) {
        if (file == null) return

        ManifestModel.fromFile(file).openGLESVersion.let { (value, element) ->
          if (value < 0x00020000) {
            holder.registerProblem(element
                    ?: file, message("no.opengl.directive.problem.descriptor") + (if (element == null) ". " + message("no.opengl.html.description") else ""))
          }
        }
      }

    }
  }

}
