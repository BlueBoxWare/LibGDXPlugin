package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileWithId
import org.jdom.Attribute
import org.jdom.Element

/**
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/306d705e1829bd3c74afc2489bfb7ed59d686b84/platform/lang-impl/src/com/intellij/openapi/file/exclude/PersistentFileSetManager.java
 *
 */
open class PersistentFileSetManager : PersistentStateComponent<Element> {

    val files = HashSet<String>()

    fun add(file: VirtualFile): Boolean {
        if (file !is VirtualFileWithId || file.isDirectory) {
            return false
        }
        files.add(VfsUtilCore.pathToUrl(file.path))
        return true
    }

    fun remove(file: VirtualFile): Boolean {
        val url = VfsUtilCore.pathToUrl(file.path)
        if (!files.contains(url)) {
            return false
        }
        files.remove(url)
        return true
    }

    fun removeAll() = files.clear()

    fun contains(file: VirtualFile) = files.contains(VfsUtilCore.pathToUrl(file.path))

    fun contains(url: String) = files.contains(url)

    override fun loadState(state: Element) {
        for (child in state.getChildren("file") ?: listOf()) {
            if (child is Element) {
                child.getAttribute("url")?.let { filePathAttr ->
                    filePathAttr.value?.let { files.add(it) }
                }
            }
        }
    }

    override fun getState(): Element {
        val root = Element("root")

        for (file in files.sorted()) {
            val element = Element("file")
            val filePathAttr = Attribute("url", file)
            element.setAttribute(filePathAttr)
            root.addContent(element)
        }

        return root
    }
}
