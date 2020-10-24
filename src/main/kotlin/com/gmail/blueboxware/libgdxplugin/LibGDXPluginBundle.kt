/*
 * Adapted from https://upsource.jetbrains.com/idea-ce/file/android-0b35b2f9060a1eb60b457737c07c49e03d97173b/platform/analysis-api/src/com/intellij/codeInspection/InspectionsBundle.java
 */
package com.gmail.blueboxware.libgdxplugin

import com.intellij.AbstractBundle
import com.intellij.CommonBundle
import org.jetbrains.annotations.PropertyKey
import java.lang.ref.Reference
import java.lang.ref.SoftReference
import java.util.*

private var ourBundle: Reference<ResourceBundle>? = null

private const val BUNDLE = "libgdxplugin"

fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any, default: String? = null): String {

  getBundle()?.let { bundle ->
    return if (default == null) {
      AbstractBundle.message(bundle, key, *params)
    } else {
      AbstractBundle.messageOrDefault(bundle, key, default, *params)
    }
  }

  return ""
}

fun getBundle(): ResourceBundle? {
  var bundle = com.intellij.reference.SoftReference.dereference(ourBundle)

  if (bundle == null) {
    bundle = ResourceBundle.getBundle(BUNDLE)
    ourBundle = SoftReference<ResourceBundle>(bundle)
  }

  return bundle
}