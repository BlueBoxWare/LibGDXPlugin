package com.gmail.blueboxware.libgdxplugin.properties

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase


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
abstract class PropertiesCodeInsightFixtureTestCase : LibGDXCodeInsightFixtureTestCase() {

    override fun setUp() {
        super.setUp()

        addLibGDX()
        addAnnotations()

        listOf(
            "messages.properties",
            "messages_de.properties",
            "messages_en_GB.properties",
            "messages_fr_CA_VAR1.properties",
            "test.properties",
            "test_es.properties",
            "extra.properties"
        ).forEach {
            copyFileToProject(it)
        }

    }

    override fun getBasePath(): String = "filetypes/properties"
}
