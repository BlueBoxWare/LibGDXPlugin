package com.gmail.blueboxware.libgdxplugin.ui

import com.intellij.debugger.engine.evaluation.EvaluationContext
import com.intellij.debugger.settings.NodeRendererSettings
import com.intellij.debugger.ui.tree.ValueDescriptor
import com.intellij.debugger.ui.tree.render.CompoundReferenceRenderer
import com.intellij.debugger.ui.tree.render.DescriptorLabelListener
import com.intellij.util.ui.ColorIcon
import com.sun.jdi.ClassNotPreparedException
import com.sun.jdi.FloatValue
import com.sun.jdi.ObjectReference
import java.awt.Color
import java.lang.IllegalArgumentException
import javax.swing.Icon

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
class LibGDXColorObjectRenderer(rendererSettings: NodeRendererSettings): CompoundReferenceRenderer(
        rendererSettings,
        "LibGDX Color",
        null,
        null
) {

  init {
    className = "com.badlogic.gdx.graphics.Color"
    isEnabled = true
  }

  override fun calcValueIcon(descriptor: ValueDescriptor?, evaluationContext: EvaluationContext?, listener: DescriptorLabelListener?): Icon? {

    val value = descriptor?.value as? ObjectReference ?: return null

    val r = getValue(value, "r")
    val g = getValue(value, "g")
    val b = getValue(value, "b")
    val a = getValue(value, "a")

    val color = Color(r, g, b, a)

    return ColorIcon(16, 12, color, true)
  }

  private fun getValue(objectReference: ObjectReference, fieldName: String): Float {
    try {
      val field = objectReference.referenceType()?.fieldByName(fieldName)
      return (objectReference.getValue(field) as? FloatValue)?.value() ?: 0f
    } catch (e: IllegalArgumentException) {
      // Nothing
    } catch (e: ClassNotPreparedException) {
      // Nothing
    }

    return 0f
  }

}