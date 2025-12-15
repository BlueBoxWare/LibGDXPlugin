package com.gmail.blueboxware.libgdxplugin.utils.androidManifest

import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag

class ManifestModel {

    var minSDK: ManifestValue<Int> = ManifestValue(1)
    var maxSDK: ManifestValue<Int>? = null
    var targetSDK: ManifestValue<Int>? = null

    var openGLESVersion: ManifestValue<Int> = ManifestValue(0x00010000)

    val permissions: MutableList<ManifestValue<String>> = mutableListOf()

    companion object {

        fun fromFile(manifestFile: XmlFile): ManifestModel {

            val model = ManifestModel()

            manifestFile.accept(object : ManifestVisitor() {

                override fun processOpenGLESVersion(value: Int, element: XmlTag) {
                    if (model.openGLESVersion.element == null || value >= model.openGLESVersion.value) {
                        model.openGLESVersion = ManifestValue(value, element)
                    }
                }

                override fun processMinSDKVersion(value: Int, element: XmlAttribute) {
                    model.minSDK = ManifestValue(value, element)
                }

                override fun processTargetSDKVersion(value: Int, element: XmlAttribute) {
                    model.targetSDK = ManifestValue(value, element)
                }

                override fun processMaxSDKVersion(value: Int, element: XmlAttribute) {
                    model.maxSDK = ManifestValue(value, element)
                }

                override fun processPermission(value: String, element: XmlTag) {
                    model.permissions.add(ManifestValue(value, element))
                }

            })

            return model

        }

    }

}
