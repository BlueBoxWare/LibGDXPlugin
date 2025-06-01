package com.gmail.blueboxware.libgdxplugin.versions.libs

import com.gmail.blueboxware.libgdxplugin.versions.Libraries
import com.gmail.blueboxware.libgdxplugin.versions.Library
import com.gmail.blueboxware.libgdxplugin.versions.Repository
import com.gmail.blueboxware.libgdxplugin.versions.VersionService

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
internal class LibGDXLibrary(
    name: String,
    groupId: String,
    artifactId: String,
    reposity: Repository = Repository.MAVEN_CENTRAL
) : Library(name, groupId, artifactId, reposity) {

    override fun getLatestVersion(versionService: VersionService) =
        versionService.getLatestVersion(Libraries.LIBGDX)

    override suspend fun updateLatestVersion(versionService: VersionService, networkAllowed: Boolean): Boolean =
        false

}
