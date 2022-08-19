@file:Suppress("RegExpUnnecessaryNonCapturingGroup", "RegExpSimplifiable")

package com.gmail.blueboxware.libgdxplugin.utils

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.LibGDXSkinFileType
import com.gmail.blueboxware.libgdxplugin.settings.LibGDXProjectSkinFiles
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FileTypeIndex
import org.jetbrains.kotlin.idea.search.allScope

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
private const val identifier = """\p{javaJavaIdentifierStart}\p{javaJavaIdentifierPart}*"""
private const val className = """[\p{javaJavaIdentifierStart}&&[\p{Lu}]]\p{javaJavaIdentifierPart}*"""
private const val fqClassName = """$identifier(?:\.$identifier)*(?:\.$className)"""
private const val commonClassNames = """(?:Color|BitmapFont|TintedDrawable|ButtonStyle)"""

@Suppress("RegExpRedundantNestedCharacterClass")
val SKIN_SIGNATURE = Regex("""(?:com\.badlogic\.gdx\.$fqClassName|\b$commonClassNames)\s*["']?\s*:\s*\{""")

fun getSkinFiles(project: Project): List<VirtualFile> {
    val result = mutableListOf<VirtualFile>()
    result.addAll(FileTypeIndex.getFiles(LibGDXSkinFileType.INSTANCE, project.allScope()))
    project.getComponent(LibGDXProjectSkinFiles::class.java)?.let { result.addAll(it.files) }
    return result.filter { it.isValid }.toList()
}





