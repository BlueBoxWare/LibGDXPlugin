package com.gmail.blueboxware.libgdxplugin.ui

import com.intellij.ui.JBColor
import com.intellij.util.ui.UIUtil
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Label
import java.awt.image.BufferedImage
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.JPanel

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
class ImagePreviewComponent(val image: BufferedImage, description: String): JPanel() {

  init {
    val innerComponent = JPanel(BorderLayout(6, 6))

    innerComponent.add(MyImageComponent(), BorderLayout.CENTER)
    innerComponent.add(Label(description), BorderLayout.SOUTH)

    innerComponent.border = BorderFactory.createEmptyBorder(4, 4, 4, 4)
    innerComponent.background = UIUtil.getToolTipBackground()

    add(innerComponent)

    background = UIUtil.getToolTipBackground()
    border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(JBColor.BLACK),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
    )
  }

  private inner class MyImageComponent: JComponent() {

    private val prefSize: Dimension = Dimension(image.width, image.height)

    override fun paint(g: Graphics?) {
      super.paint(g)

      bounds?.let { bounds ->

        val width = if (bounds.width > image.width) image.width else bounds.width
        val height = if (bounds.height > image.height) image.height else bounds.height

        g?.drawImage(
                image,
                bounds.width / 2 - width / 2,
                0,
                width,
                height,
                this
        )
      }

    }

    override fun getPreferredSize() = prefSize

    override fun getMaximumSize() = prefSize

    override fun getMinimumSize() = prefSize

  }

}

