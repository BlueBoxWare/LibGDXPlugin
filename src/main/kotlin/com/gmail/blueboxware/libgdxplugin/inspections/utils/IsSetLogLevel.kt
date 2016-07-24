package com.gmail.blueboxware.libgdxplugin.inspections.utils

import com.intellij.psi.PsiClass

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

fun isSetLogLevel(clazz: PsiClass, methodName: String): Boolean {

  val applicationClass = "com.badlogic.gdx.Application"
  val loggerClass = "com.badlogic.gdx.utils.Logger"

  if (clazz.qualifiedName == applicationClass) return methodName == "setLogLevel"
  if (clazz.qualifiedName == loggerClass) return methodName == "setLevel"

  for (superClass in clazz.supers.flatMap { it.supers.toList() }) {
    if (superClass.qualifiedName == applicationClass) {
      return methodName == "setLogLevel"
    } else if (superClass.qualifiedName == loggerClass) {
      return methodName == "setLevel"
    }
  }

  return false
}


