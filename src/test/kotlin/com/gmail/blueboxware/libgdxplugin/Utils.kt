package com.gmail.blueboxware.libgdxplugin

import com.gmail.blueboxware.libgdxplugin.filetypes.atlas2.Atlas2File
import com.gmail.blueboxware.libgdxplugin.utils.markFileAsGdxJson
import com.intellij.psi.PsiFile
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.UsefulTestCase
import java.io.BufferedReader
import java.io.File


/*
 * Copyright 2018 Blue Box Ware
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
internal fun UsefulTestCase.testname() = if (this.name == null) "" else PlatformTestUtil.getTestName(name, true)

internal fun PsiFile.markAsGdxJson() = project.markFileAsGdxJson(virtualFile)

internal fun loadAtlas(file: Atlas2File): TextureAtlas {
    val pages = mutableListOf<AtlasPage>()
    val regions = mutableListOf<AtlasRegion>()
    var hasIndexes = false

    file.getPages().forEach { p ->
        val page = AtlasPage()
        page.textureFile = p.header.text
        p.getFieldValues("size")?.map { it.toInt() }?.let { s ->
            page.width = s[0]
            page.height = s[1]
        }
        p.getFieldValue("format")?.let { f ->
            page.format = f
        }
        p.getFieldValues("filter")?.let { f ->
            page.minFilter = f[0]
            page.magFilter = f[1]
        }
        p.getFieldValue("repeat")?.let { r ->
            if (r.contains('x')) page.uWrap = "Repeat"
            if (r.contains('y')) page.vWrap = "Repeat"
        }
        p.getFieldValue("pma")?.let { pma ->
            page.pma = pma == "true"
        }
        pages.add(page)

        p.regionList.forEach { r ->
            val region = AtlasRegion()
            val names = mutableListOf<String>()
            val values = mutableListOf<List<Int>>()

            region.file = page.textureFile
            region.name = r.header.text
            r.getFieldValues("xy")?.map { it.toInt() }?.let { xy ->
                region.left = xy[0]
                region.top = xy[1]
            }
            r.getFieldValues("size")?.map { it.toInt() }?.let { s ->
                region.width = s[0]
                region.height = s[1]
            }
            r.getFieldValues("bounds")?.map { it.toInt() }?.let { b ->
                region.left = b[0]
                region.top = b[1]
                region.width = b[2]
                region.height = b[3]
            }
            r.getFieldValues("offset")?.map { it.toInt() }?.let { o ->
                region.offsetX = o[0]
                region.offsetY = o[1]
            }
            r.getFieldValues("orig")?.map { it.toInt() }?.let { o ->
                region.originalWidth = o[0]
                region.originalHeight = o[1]
            }
            r.getFieldValues("offsets")?.let { o ->
                region.offsetX = o[0].toInt()
                region.offsetY = o[1].toInt()
                region.originalWidth = o[2].toInt()
                region.originalHeight = o[3].toInt()
            }
            r.getFieldValue("rotate")?.let { rotate ->
                if (rotate == "true") {
                    region.degrees = 90
                } else if (rotate != "false") {
                    region.degrees = rotate.toInt()
                }
                region.rotate = region.degrees == 90
            }
            r.getFieldValue("index")?.let { i ->
                region.index = i.toInt()
                if (region.index != -1) {
                    hasIndexes = true
                }
            }
            r.getFieldList()
                .filter { it.key !in listOf("xy", "size", "bounds", "offset", "orig", "offsets", "rotate", "index") }
                .forEach { field ->
                    names.add(field.key)
                    values.add(field.values.map { it.toIntOrNull() ?: 0 })
                }

            if (region.originalWidth == 0 && region.originalHeight == 0) {
                region.originalWidth = region.width
                region.originalHeight = region.height
            }

            if (names.isNotEmpty()) {
                region.names = names
                region.values = values
            }

            regions.add(region)
        }
    }

    if (hasIndexes) {
        regions.sortWith { r1, r2 ->
            var i1 = r1.index
            if (i1 == -1) i1 = Int.MAX_VALUE
            var i2 = r2.index
            if (i2 == -1) i2 = Int.MAX_VALUE
            return@sortWith i1 - i2
        }
    }

    return TextureAtlas(pages, regions)
}

internal fun loadAtlas(packFile: File): TextureAtlas {
    val pages = mutableListOf<AtlasPage>()
    val regions = mutableListOf<AtlasRegion>()
    val entry = mutableListOf("", "", "", "", "")
    var hasIndexes = false

    val pageFields: Map<String, (AtlasPage) -> Unit> = mapOf("size" to { p ->
        p.width = entry[1].toInt()
        p.height = entry[2].toInt()
    }, "format" to { p ->
        p.format = entry[1]
    }, "filter" to { p ->
        p.minFilter = entry[1]
        p.magFilter = entry[2]
    }, "repeat" to { p ->
        if (entry[1].indexOf('x') != -1) p.uWrap = "Repeat"
        if (entry[1].indexOf('y') != -1) p.vWrap = "Repeat"
    }, "pma" to { p ->
        p.pma = entry[1] == "true"
    })

    val regionFields: Map<String, (AtlasRegion) -> Unit> = mapOf("xy" to { r ->
        r.left = entry[1].toInt()
        r.top = entry[2].toInt()
    }, "size" to { r ->
        r.width = entry[1].toInt()
        r.height = entry[2].toInt()
    }, "bounds" to { r ->
        r.left = entry[1].toInt()
        r.top = entry[2].toInt()
        r.width = entry[3].toInt()
        r.height = entry[4].toInt()
    }, "offset" to { r ->
        r.offsetX = entry[1].toInt()
        r.offsetY = entry[2].toInt()
    }, "orig" to { r ->
        r.originalWidth = entry[1].toInt()
        r.originalHeight = entry[2].toInt()
    }, "offsets" to { r ->
        r.offsetX = entry[1].toInt()
        r.offsetY = entry[2].toInt()
        r.originalWidth = entry[3].toInt()
        r.originalHeight = entry[4].toInt()
    }, "rotate" to { r ->
        val value = entry[1]
        if (value == "true") {
            r.degrees = 90
        } else if (value != "false") {
            r.degrees = value.toInt()
        }
        r.rotate = r.degrees == 90
    }, "index" to { r ->
        r.index = entry[1].toInt()
        if (r.index != -1) {
            hasIndexes = true
        }
    })

    val reader = BufferedReader(packFile.reader())
    var line = reader.readLine()

    // Ignore empty lines before first entry
    while (line != null && line.trim().isEmpty()) {
        line = reader.readLine()
    }

    // Header fields
    while (true) {
        if (line == null || line.trim().isEmpty()) break
        if (readEntry(entry, line) == 0) break // ignore header fields
        line = reader.readLine()
    }

    // Page and region entries
    var page: AtlasPage? = null
    val names = mutableListOf<String>()
    val values = mutableListOf<List<Int>>()
    while (true) {
        if (line == null) break
        if (line.trim().isEmpty()) {
            page = null
            line = reader.readLine()
        } else if (page == null) {
            // Page start
            page = AtlasPage()
            page.textureFile = line
            while (true) {
                line = reader.readLine()
                if (readEntry(entry, line) == 0) break
                val field = pageFields[entry[0]]
                if (field != null) field(page)
            }
            pages.add(page)
        } else {
            val region = AtlasRegion()
            region.name = line.trim()
            region.file = page.textureFile
            while (true) {
                line = reader.readLine()
                val count = readEntry(entry, line)
                if (count == 0) break
                val field = regionFields[entry[0]]
                if (field != null) {
                    field(region)
                } else {
                    names.add(entry[0])
                    val entryValues = MutableList(count) { 0 }
                    for (i in 0 until count) {
                        entryValues[i] = entry[i + 1].toIntOrNull() ?: 0
                    }
                    values.add(entryValues)
                }
            }
            if (region.originalWidth == 0 && region.originalHeight == 0) {
                region.originalWidth = region.width
                region.originalHeight = region.height
            }
            if (names.isNotEmpty()) {
                region.names = names.toList()
                region.values = values.toList()
                names.clear()
                values.clear()
            }
            regions.add(region)
        }


    }

    if (hasIndexes) {
        regions.sortWith { r1, r2 ->
            var i1 = r1.index
            if (i1 == -1) i1 = Int.MAX_VALUE
            var i2 = r2.index
            if (i2 == -1) i2 = Int.MAX_VALUE
            return@sortWith i1 - i2
        }
    }

    return TextureAtlas(pages, regions)

}

internal data class TextureAtlas(
    private val pages: List<AtlasPage>, private val regions: List<AtlasRegion>
)

internal data class AtlasRegion(
    var name: String = "",
    var file: String = "",
    var left: Int = 0,
    var top: Int = 0,
    var width: Int = 0,
    var height: Int = 0,
    var offsetX: Int = 0,
    var offsetY: Int = 0,
    var originalWidth: Int = 0,
    var originalHeight: Int = 0,
    var rotate: Boolean = false,
    var degrees: Int = 0,
    var index: Int = -1,
    var names: List<String> = listOf(),
    var values: List<List<Int>> = listOf()
)

internal data class AtlasPage(
    var width: Int = 0,
    var height: Int = 0,
    var format: String = "RGBA8888",
    var minFilter: String = "Nearest",
    var magFilter: String = "Nearest",
    var uWrap: String = "ClampToEdge",
    var vWrap: String = "ClampToEdge",
    var pma: Boolean = false,
    var textureFile: String = "",
    var regions: MutableList<AtlasRegion> = mutableListOf()
)

private fun readEntry(entry: MutableList<String>, l: String?): Int {
    if (l == null) return 0
    var line = l
    line = line.trim()
    if (line.isEmpty()) return 0
    val colon = line.indexOf(':')
    if (colon == -1) return 0
    entry[0] = line.take(colon).trim()
    var lastMatch = colon + 1
    var i = 1
    while (true) {
        val comma = line.indexOf(',', lastMatch)
        if (comma == -1) {
            entry[i] = line.substring(lastMatch).trim()
            return i
        }
        entry[i] = line.substring(lastMatch, comma).trim()
        lastMatch = comma + 1
        if (i == 4) return 4
        i++
    }
}
