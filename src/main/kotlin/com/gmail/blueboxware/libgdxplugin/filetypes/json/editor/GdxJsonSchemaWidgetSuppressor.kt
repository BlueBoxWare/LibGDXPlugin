package com.gmail.blueboxware.libgdxplugin.filetypes.json.editor

import com.gmail.blueboxware.libgdxplugin.filetypes.json.LibGDXJsonLanuage
import com.intellij.lang.LanguageUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.jsonSchema.extension.JsonWidgetSuppressor


/*
 * Copyright 2021 Blue Box Ware
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
internal class GdxJsonSchemaWidgetSuppressor : JsonWidgetSuppressor {

    override fun suppressSwitcherWidget(file: VirtualFile, project: Project): Boolean =
        LanguageUtil.getLanguageForPsi(project, file) == LibGDXJsonLanuage

}
