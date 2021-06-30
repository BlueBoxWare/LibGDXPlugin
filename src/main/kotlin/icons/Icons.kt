package icons

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import com.intellij.util.IconUtil
import java.awt.Color

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
object Icons {

  private val LIBGDX_FILETYPE = IconLoader.getIcon("/icons/LibGDX.png", Icons::class.java)
  val SKIN_FILETYPE = IconLoader.getIcon("/icons/LibGDXSkin.png", Icons::class.java)
  val ATLAS_FILETYPE = LIBGDX_FILETYPE
  val FONT_FILETYPE = IconLoader.getIcon("/icons/LibGDXBitmapFont.png", Icons::class.java)
  val LIBGDX_JSON_FILETYPE = IconUtil.colorize(AllIcons.FileTypes.Json, Color(39, 128, 233))

}
