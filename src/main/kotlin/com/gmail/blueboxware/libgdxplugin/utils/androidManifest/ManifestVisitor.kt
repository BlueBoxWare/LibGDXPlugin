package com.gmail.blueboxware.libgdxplugin.utils.androidManifest

import com.intellij.psi.XmlRecursiveElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import java.lang.NumberFormatException

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
abstract class ManifestVisitor : XmlRecursiveElementVisitor() {

    abstract fun processOpenGLESVersion(value: Int, element: XmlTag)

    abstract fun processMinSDKVersion(value: Int, element: XmlAttribute)
    abstract fun processTargetSDKVersion(value: Int, element: XmlAttribute)
    abstract fun processMaxSDKVersion(value: Int, element: XmlAttribute)

    abstract fun processSupportsScreens(
        value: SupportsScreens,
        element: XmlTag,
        hasLargeScreensSupportAttribute: Boolean,
        hasXLargeScreensSupportAttribute: Boolean
    )

    abstract fun processPermission(value: String, element: XmlTag)

    override fun visitXmlTag(tag: XmlTag?) {

        if (tag?.name == "uses-feature" && tag.parentTag?.name == "manifest") {
            tag.getAttribute("android:glEsVersion")?.value?.let { value ->
                try {
                    processOpenGLESVersion(Integer.decode(value), tag)
                } catch (e: NumberFormatException) {
                    // Nothing
                }
            }
        } else if (tag?.name == "uses-sdk" && tag.parentTag?.name == "manifest") {
            tag.getAttribute("android:minSdkVersion")?.let { attribute ->
                attribute.value?.toIntOrNull()?.let { value ->
                    processMinSDKVersion(value, attribute)
                }
            }
            tag.getAttribute("android:targetSdkVersion")?.let { attribute ->
                attribute.value?.toIntOrNull()?.let { value ->
                    processTargetSDKVersion(value, attribute)
                }
            }
            tag.getAttribute("android:maxSdkVersion")?.let { attribute ->
                attribute.value?.toIntOrNull()?.let { value ->
                    processMaxSDKVersion(value, attribute)
                }
            }
        } else if (tag?.name == "supports-screens" && tag.parentTag?.name == "manifest") {
            val supportsScreens = SupportsScreens()
            var hasExplicitLarge = false
            var hasExplicitXLarge = false
            tag.getAttribute("android:smallScreens")?.value?.let {
                supportsScreens.smallScreens = it == "true"
            }
            tag.getAttribute("android:normalScreens")?.value?.let {
                supportsScreens.normalScreens = it == "true"
            }
            tag.getAttribute("android:largeScreens")?.value?.let {
                supportsScreens.largeScreens = it == "true"
                hasExplicitLarge = true
            }
            tag.getAttribute("android:xlargeScreens")?.value?.let {
                supportsScreens.xlargeScreens = it == "true"
                hasExplicitXLarge = true
            }
            processSupportsScreens(supportsScreens, tag, hasExplicitLarge, hasExplicitXLarge)
        } else if (tag?.name == "uses-permission" && tag.parentTag?.name == "manifest") {
            tag.getAttribute("android:name")?.value?.let {
                processPermission(it, tag)
            }
        }

        super.visitXmlTag(tag)
    }
}
