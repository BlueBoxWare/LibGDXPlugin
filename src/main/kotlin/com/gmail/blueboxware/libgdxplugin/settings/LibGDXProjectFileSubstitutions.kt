package com.gmail.blueboxware.libgdxplugin.settings

import com.gmail.blueboxware.libgdxplugin.utils.PersistentFileSetManager
import com.intellij.openapi.components.State

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
@State(name = "LibGDXSkins")
class LibGDXProjectSkinFiles : PersistentFileSetManager()

@State(name = "LibGDXNonSkins")
class LibGDXProjectNonSkinFiles : PersistentFileSetManager()

@State(name = "LibGDXGdxJsonFiles")
class LibGDXProjectGdxJsonFiles : PersistentFileSetManager()

@State(name = "LibGDXNonGdxJsonFiles")
class LibGDXProjectNonGdxJsonFiles : PersistentFileSetManager()

