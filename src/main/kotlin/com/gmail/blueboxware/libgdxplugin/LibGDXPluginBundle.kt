/*
 * Adapted from https://upsource.jetbrains.com/idea-ce/file/android-0b35b2f9060a1eb60b457737c07c49e03d97173b/platform/analysis-api/src/com/intellij/codeInspection/InspectionsBundle.java
 */
package com.gmail.blueboxware.libgdxplugin

import com.intellij.CommonBundle
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.jetbrains.annotations.PropertyKey
import java.lang.ref.Reference
import java.lang.ref.SoftReference
import java.util.*

private var ourBundle: Reference<ResourceBundle>? = null

private const val BUNDLE = "libgdxplugin"

fun message(@NotNull @PropertyKey(resourceBundle = BUNDLE)key: String, @NotNull vararg params: Any, @Nullable default: String? = null): String {

  val bundle = getBundle()

  bundle?.let { bundle ->
    if (default == null) {
      return CommonBundle.message(bundle, key, params)
    }
    else {
      return CommonBundle.messageOrDefault(bundle, key, default, params)
    }
  }

  return ""
}

@Nullable
fun getBundle(): ResourceBundle? {
  var bundle = com.intellij.reference.SoftReference.dereference(ourBundle)

  if (bundle == null) {
    bundle = ResourceBundle.getBundle(BUNDLE)
    ourBundle = SoftReference<ResourceBundle>(bundle)
  }

  return bundle
}