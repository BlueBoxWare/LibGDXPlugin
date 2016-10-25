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
package com.gmail.blueboxware.libgdxplugin.inspections.java

import com.gmail.blueboxware.libgdxplugin.components.LibGDXProjectComponent
import com.gmail.blueboxware.libgdxplugin.utils.GDXLibrary
import com.gmail.blueboxware.libgdxplugin.utils.compareVersionStrings
import com.gmail.blueboxware.libgdxplugin.message
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiNewExpression

class JavaShapeRenderer64BitCrashInspection : LibGDXJavaBaseInspection() {

  override fun getStaticDescription() = message("shaperenderer.64bit.crash.html.description")

  override fun getID() = "LibGDXShapeRendererCrash"

  override fun getDisplayName() = message("shaperenderer.64bit.crash.inspection.name")

  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean) = object: JavaElementVisitor() {

    override fun visitNewExpression(expression: PsiNewExpression?) {
      super.visitNewExpression(expression)

      if (expression == null) return

      if (expression.classReference?.qualifiedName == "com.badlogic.gdx.graphics.glutils.ShapeRenderer") {

        expression.project.getComponent(LibGDXProjectComponent::class.java)?.let { projectComponent ->
          val gdxVersion = projectComponent.getUsedLibraryVersion(GDXLibrary.GDX)

          if (gdxVersion != null && compareVersionStrings(gdxVersion, "1.9.0") >= 0 && compareVersionStrings(gdxVersion, "1.9.2") < 0) {
            holder.registerProblem(expression, message("shaperenderer.64bit.crash.problem.descriptor"))
          }
        }

      }
    }
  }
}

