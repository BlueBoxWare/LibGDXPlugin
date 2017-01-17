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
package com.gmail.blueboxware.libgdxplugin.inspections;

import com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections.SkinMalformedColorStringInspection;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections.SkinNonExistingClassInspection;
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections.SkinNonExistingFieldInspection;
import com.gmail.blueboxware.libgdxplugin.inspections.android.DesignedForTabletsInspection;
import com.gmail.blueboxware.libgdxplugin.inspections.android.OpenGLESDirectiveInspection;
import com.gmail.blueboxware.libgdxplugin.inspections.gradle.GradleOutdatedVersionsInspection;
import com.gmail.blueboxware.libgdxplugin.inspections.java.*;
import com.gmail.blueboxware.libgdxplugin.inspections.kotlin.*;
import com.gmail.blueboxware.libgdxplugin.inspections.xml.XmlTestIdsInspection;
import com.intellij.codeInspection.InspectionToolProvider;

public class LibGDXInspectionProvider implements InspectionToolProvider {

    @Override
    public Class[] getInspectionClasses() {
        return new Class[]{
                JavaUnsafeIteratorInspection.class,
                JavaTestIdsInspection.class,
                JavaStaticResourceInspection.class,
                JavaMissingFlushInspection.class,
                JavaProfilingCodeInspection.class,
                JavaShapeRenderer64BitCrashInspection.class,
                JavaFlushInsideLoopInspection.class,
                JavaLogLevelInspection.class,

                KotlinUnsafeIteratorInspection.class,
                KotlinTestIdsInspection.class,
                KotlinStaticResourceInspection.class,
                KotlinMissingFlushInspection.class,
                KotlinProfilingCodeInspection.class,
                KotlinShapeRenderer64BitCrashInspection.class,
                KotlinFlushInsideLoopInspection.class,
                KotlinLogLevelInspection.class,

                OpenGLESDirectiveInspection.class,
                DesignedForTabletsInspection.class,

                GradleOutdatedVersionsInspection.class,

                XmlTestIdsInspection.class,

                SkinNonExistingClassInspection.class,
                SkinNonExistingFieldInspection.class,
                SkinMalformedColorStringInspection.class
        };
    }
}
