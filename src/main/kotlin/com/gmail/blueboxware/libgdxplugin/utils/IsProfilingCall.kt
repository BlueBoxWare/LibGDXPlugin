package com.gmail.blueboxware.libgdxplugin.utils

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

fun isProfilingCall(fqClassName: String, methodName: String): Boolean {

    if (fqClassName == "com.badlogic.gdx.graphics.FPSLogger" && methodName == "log") {

        return true

    } else if (fqClassName == "com.badlogic.gdx.utils.PerformanceCounter") {

        if (methodName == "start" || methodName == "tick" || methodName == "stop" || methodName == "reset") {
            return true
        }

    } else if (fqClassName == "com.badlogic.gdx.utils.PerformanceCounters" && methodName == "tick") {

        return true

    } else if (
        fqClassName == "com.badlogic.gdx.graphics.profiling.GLProfiler"
        || fqClassName == "com.badlogic.gdx.graphics.profiling.GL20Profiler"
        || fqClassName == "com.badlogic.gdx.graphics.profiling.GL30Profiler"
    ) {

        if (methodName == "enable") {
            return true
        }

    }

    return false
}

