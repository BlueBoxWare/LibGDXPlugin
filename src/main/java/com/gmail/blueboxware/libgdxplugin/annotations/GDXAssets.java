package com.gmail.blueboxware.libgdxplugin.annotations;
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

import java.lang.annotation.*;

/**
 * An annotation for use with <a href="https://github.com/BlueBoxWare/LibGDXPlugin">LibGDXPlugin</a>.<p>
 *
 * Use this annotation on a field of type Skin, TextureAtlas or I18NBundle in Java (or a property of one of those
 * types in Kotlin) to specify which Skin JSON files, Texture Atlas files and .properties files are being used by
 * that Skin, Atlas or Bundle.
 * LibGDXPlugin will use the specified files to provide Completion, Go to Definition, Find Usages, Diagnostics and
 * Rename Refactoring where appropriate.<p>
 *
 * All files should be specified relative to the Project Root.<p>
 * For properties files, specify the base bundle (including the .properties extension)<p>
 *
 * Skin example (Java):
 * <blockquote><pre>
 *
 *   {@literal @}GDXAssets(skinFiles = {"android/assets/ui.skin"})
 *    Skin uiSkin = new Skin(Gdx.files.internal("assets/ui.skin"));
 *
 * </pre></blockquote><p>
 *
 * TextureAtlas example (Java):
 * <blockquote><pre>
 *
 *   {@literal @}GDXAssets(atlasFiles = {"android/assets/textures.atlas"})
 *    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("assets/textures.atlas"));
 *
 * </pre></blockquote><p>
 *
 * Properties example (Java):
 * <blockquote><pre>
 *
 *   {@literal @}GDXAssets(propertiesFiles = {"android/assets/i18n/Messages.properties"})
 *   I18NBundle bundle = I18NBundle.createBundle(Gdx.files.internal("i18n/Messages"), locale);
 *
 * </pre></blockquote>
 *
 * Skin example (Kotlin):
 * <blockquote><pre>
 *
 *    {@literal @}GDXAssets(skinFiles = arrayOf("android/assets/ui.skin"))
 *     val s: Skin = Skin(Gdx.files.internal("assets/ui.skin"))
 *
 * </pre></blockquote><p>
 *
 * When no Atlas files are specified and a file with same name (or multiple files, if you specify multiple Skin files)
 * in the same directory with a ".atlas" extension exists, that file is used as Atlas file.<p>
 *
 * You can specify multiple Skin and Atlas files:<p>
 *
 * Example (Java):
 * <blockquote><pre>
 *
 *   {@literal @}GDXAssets(
 *      skinFiles = {"android/assets/ui.skin", "android/assets/extra.skin"},
 *      atlasFiles = {"android/assets/ui.atlas", "android/assets/icons.atlas"}
 *    )
 *    Skin uiSkin = new Skin(Gdx.files.internal("assets/ui.skin"));
 *    // ...
 *    uiSkin.load(Gdx.files.internal("android/assets/extra.skin"))
 *
 * </pre></blockquote><p>
 *
 * Go to Definition, Find Usages and Smart Renaming are only available when the specified Skin files
 * and Atlas files are registered by Idea as Skin files and Atlas files respectively.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE})
public @interface GDXAssets {

  String[] skinFiles() default "";
  String[] atlasFiles() default "";
  String[] propertiesFiles() default "";

}
