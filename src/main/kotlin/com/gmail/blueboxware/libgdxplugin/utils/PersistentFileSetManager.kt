package com.gmail.blueboxware.libgdxplugin.utils

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.VirtualFileWithId
import gnu.trove.THashSet
import org.jdom.Attribute
import org.jdom.Element

/**
 *
 * Adapted from https://github.com/JetBrains/intellij-community/blob/306d705e1829bd3c74afc2489bfb7ed59d686b84/platform/lang-impl/src/com/intellij/openapi/file/exclude/PersistentFileSetManager.java
 *
 */
open class PersistentFileSetManager: PersistentStateComponent<Element> {

  val files = THashSet<VirtualFile>()

  fun add(file: VirtualFile): Boolean {
    if (file !is VirtualFileWithId || file.isDirectory) {
      return false
    }
    files.add(file)
    return true
  }

  fun remove(file: VirtualFile): Boolean {
    if (!files.contains(file)) {
      return false
    }
    files.remove(file)
    return true
  }

  fun removeAll() = files.clear()

  fun contains(file: VirtualFile) = files.contains(file)

  private fun getSortedFiles(): Collection<VirtualFile> {
    val sortedFiles = mutableListOf<VirtualFile>()
    sortedFiles.addAll(files)
    sortedFiles.sortWith { o1, o2 -> o1.path.toLowerCase().compareTo(o2.path.toLowerCase()) }
    return sortedFiles
  }

  override fun loadState(state: Element) {
    val vfManager = VirtualFileManager.getInstance()
    for (child in state.getChildren("file") ?: listOf()) {
      if (child is Element) {
        child.getAttribute("url")?.let { filePathAttr ->
          val filePath = filePathAttr.value
          val virtualFile = vfManager.findFileByUrl(filePath)
          if (virtualFile != null) {
            files.add(virtualFile)
          }
        }
      }
    }
  }

  override fun getState(): Element {
    val root = Element("root")

    for (virtualFile in getSortedFiles()) {
      val element = Element("file")
      val filePathAttr = Attribute("url", VfsUtilCore.pathToUrl(virtualFile.path))
      element.setAttribute(filePathAttr)
      root.addContent(element)
    }

    return root
  }
}