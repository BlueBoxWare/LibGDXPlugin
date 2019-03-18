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
package com.gmail.blueboxware.libgdxplugin.annotations;

import java.lang.annotation.*;

/**
 * An annotation for use with <a href="https://github.com/BlueBoxWare/LibGDXPlugin">LibGDXPlugin</a>.<p>
 *
 * Since version 1.9.9 LibGDX Skins support tagged classes: the ability to use short names for names of classes
 * in Skin files. In addition to the standard, "built-in" short class names it is also possible to define custom
 * short names for your own classes by overriding Skin.getJsonLoader() and calling json.addClassTag().
 *
 * LibGDXPlugin understands the default short names. It also tries to determine any custom short names by looking
 * for calls to addClassTag(), but there is only so much it can do.
 *
 * To explicitly tell the plugin to recognize one (or more) short names for one of your own classes, you can
 * use this annotation on that class.
 *
 * Example:
 * <blockquote><pre>
 *
 *    package com.something.ui;
 *
 *   {@literal @}GDXTag({"Widget"})
 *    class MyCustomWidget {
 *      // ...
 *    }
 *
 * </pre></blockquote><p>
 *
 * After this, the plugin will recognize "Widget" as a short name for "com.something.ui.MyCustomWidget" in
 * Skin files. It is of course still up to you to make LibGDX recognize this short name by subclassing Skin or
 * by some other means.
 *
 * Note that at the moment there is no way to tell the plugin a short name is only valid in specific Skin files,
 * instead of all Skin files.
 *
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface GDXTag {

  String[] value();

}
