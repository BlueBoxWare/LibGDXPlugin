package com.gmail.blueboxware.libgdxplugin.filetypes.skin.psi

import com.intellij.json.psi.JsonPsiUtil

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
object SkinPsiUtil {

  /**
   * Returns content of the string literal (without escaping) striving to preserve as much of user data as possible.
   * <ul>
   * <li>If literal length is greater than one and it starts and ends with the same quote and the last quote is not escaped, returns
   * text without first and last characters.</li>
   * <li>Otherwise if literal still begins with a quote, returns text without first character only.</li>
   * <li>Returns unmodified text in all other cases.</li>
   * </ul>
   *
   * @param text presumably result of {@link JsonStringLiteral#getText()}
   * @return
   */
  fun stripQuotes(text: String) = JsonPsiUtil.stripQuotes(text)

}