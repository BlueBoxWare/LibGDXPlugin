package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.mixins

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinObject
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.SkinProperty
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi.impl.SkinContainerImpl
import com.intellij.lang.ASTNode
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager

abstract class SkinObjectMixin(node: ASTNode) : SkinContainerImpl(node), SkinObject {

  private val propertyCache = object: CachedValueProvider<Map<String, SkinProperty>> {

    override fun compute(): CachedValueProvider.Result<Map<String, SkinProperty>>? {
      val cache = mutableMapOf<String, SkinProperty>()

      for (property in propertyList) {
        property.name?.let { name ->
          if (!cache.containsKey(name)) {
            cache.put(name, property)
          }
        }
      }

      return com.intellij.psi.util.CachedValueProvider.Result.createSingleDependency(cache, this)
    }
  }

  override fun findProperty(name: String): SkinProperty? = CachedValuesManager.getCachedValue(this, propertyCache).get(name)

}