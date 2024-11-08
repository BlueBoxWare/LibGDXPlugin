package com.gmail.blueboxware.libgdxplugin.versions

import com.gmail.blueboxware.libgdxplugin.versions.libs.LibGDXLibrary
import java.util.*

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
enum class Libraries(internal val library: Library) {

    LIBGDX(
        Library(
            "LibGDX",
            "com.badlogicgames.gdx",
            "gdx",
            extKeys = listOf("gdxVersion")
        )
    ),
    AI(
        Library(
            "Gdx AI",
            "com.badlogicgames.gdx",
            "gdx-ai",
            extKeys = listOf("aiVersion")
        )
    ),
    ANIM8(
        Library(
            "anim8-gdx",
            "com.github.tommyettinger",
            "anim8-gdx",
            extKeys = listOf("anim8Version")
        )
    ),
    ARTEMIS_ODB(
        Library(
            "Artemis-odb",
            "net.onedaybeard.artemis",
            "artemis-odb",
            extKeys = listOf("artemisOdbVersion")
        )
    ),
    ASHLEY(
        Library(
            "Ashley",
            "com.badlogicgames.ashley",
            "ashley",
            extKeys = listOf("ashleyVersion")
        )
    ),
    AUTUMN(
        Library(
            "Autumn",
            "com.github.czyzby",
            "gdx-autumn",
            extKeys = listOf("autumnVersion")
        )
    ),
    AUTUMN_MVC(
        Library(
            "Autumn MVC",
            "com.github.czyzby",
            "gdx-autumn-mvc",
            extKeys = listOf("autumnMvcVersion")
        )
    ),
    BLADE_INK(
        Library(
            "blade-ink",
            "com.bladecoder.ink",
            "blade-ink",
            extKeys = listOf("bladeInkVersion")
        )
    ),
    BOX2D(
        LibGDXLibrary(
            "Box2d",
            "com.badlogicgames.gdx",
            "gdx-box2d"
        )
    ),
    BOX2dLIGHTS(
        Library(
            "Box2dLights",
            "com.badlogicgames.box2dlights",
            "box2dlights",
            extKeys = listOf(
                "box2DLightsVersion",
                "box2dlightsVersion"
            )
        )
    ),
    BULLET(
        LibGDXLibrary(
            "GDX Bullet",
            "com.badlogicgames.gdx",
            "gdx-bullet"
        )
    ),
    COLORFUL(
        Library(
            "Colorful",
            "com.github.tommyettinger",
            "colorful",
            extKeys = listOf("colorfulVersion")
        )
    ),
    CONTROLLERS(
        LibGDXLibrary(
            "GDX Controllers",
            "com.badlogicgames.gdx",
            "gdx-controllers"
        )
    ),
    FORMIC(
        Library(
            "formic",
            "com.github.tommyettinger",
            "formic",
            extKeys = listOf("formicVersion")
        )
    ),
    FREETYPE(
        LibGDXLibrary(
            "GDX FreeType",
            "com.badlogicgames.gdx",
            "gdx-freetype"
        )
    ),
    GDX_CONTROLLER_UTILS_ADV(
        Library(
            "gdx-controllers-advanced",
            "de.golfgl.gdxcontrollerutils",
            "gdx-controllers-advanced",
            extKeys = listOf("controllerUtilsVersion")
        )
    ),
    GDX_CONTROLLER_SCENE2D(
        Library(
            "gdx-controllers-scene2d",
            "de.golfgl.gdxcontrollerutils",
            "gdx-controllerutils-scene2d",
            extKeys = listOf("controllerScene2DVersion")
        )
    ),
    GDXDIALOGS(
        Library(
            "gdx-dialogs",
            "de.tomgrill.gdxdialogs",
            "gdx-dialogs-core",
            extKeys = listOf("dialogsVersion")
        )
    ),
    GDXFACEBOOK(
        Library(
            "gdx-facebook",
            "de.tomgrill.gdxfacebook",
            "gdx-facebook-core",
            extKeys = listOf("facebookVersion")
        )
    ),
    GDXPAY(
        Library(
            "Gdx-Pay",
            "com.badlogicgames.gdxpay",
            "gdx-pay-client",
            extKeys = listOf("gdxPayVersion")
        )
    ),
    GDX_UTILS(
        Library(
            "gdx-utils",
            "com.github.tommyettinger",
            "libgdx-utils",
            extKeys = listOf("utilsVersion")
        )
    ),
    GDX_UTILS_BOX2D(
        Library(
            "gdx-utils-box2d",
            "com.github.tommyettinger",
            "libgdx-utils-box2d",
            extKeys = listOf("utilsBox2dVersion")
        )
    ),
    GDX_VFX(
        Library(
            "gdx-vfx",
            "com.crashinvaders.vfx",
            "gdx-vfx-core",
            extKeys = listOf("gdxVfxCoreVersion")
        )
    ),
    GDX_VFX_EFFECTS(
        Library(
            "gdx-vfx-effects",
            "com.crashinvaders.vfx",
            "gdx-vfx-effects",
            extKeys = listOf("gdxVfxEffectsVersion")
        )
    ),
    INGAME_CONSOLE(
        Library(
            "libGDX In-Game Console",
            "com.strongjoshua",
            "libgdx-inGameConsole",
            extKeys = listOf("inGameConsoleVersion")
        )
    ),
    JACI(
        Library(
            "Jaci",
            "com.github.ykrasik",
            "jaci-libgdx-cli-java",
            extKeys = listOf("jaciVersion")
        )
    ),
    JACI_GWT(
        Library(
            "Jaci GWT",
            "com.github.ykrasik",
            "jaci-libgdx-cli-gwt",
            extKeys = listOf("jaciGwtVersion")
        )
    ),
    JOISE(
        Library(
            "Joise",
            "com.sudoplay.joise",
            "joise",
            extKeys = listOf("joiseVersion")
        )
    ),
    KIWI(
        Library(
            "Kiwi",
            "com.github.czyzby",
            "gdx-kiwi",
            extKeys = listOf("kiwiVersion")
        )
    ),
    KTX_ACTORS(
        Library(
            "KTX actors",
            "io.github.libktx",
            "ktx-actors",
            extKeys = listOf("ktxActorsVersion")
        )
    ),
    KTX_APP(
        Library(
            "KTX app",
            "io.github.libktx",
            "ktx-app",
            extKeys = listOf("ktxAppVersion")
        )
    ),
    KTX_ASSETS(
        Library(
            "KTX assets",
            "io.github.libktx",
            "ktx-assets",
            extKeys = listOf("ktxAssetsVersion")
        )
    ),
    KTX_ASSETS_ASYNC(
        Library(
            "KTX assets-async",
            "io.github.libktx",
            "ktx-assets-async",
            extKeys = listOf("ktxAssetsAsyncVersion")
        )
    ),
    KTX_ASYNC(
        Library(
            "KTX async",
            "io.github.libktx",
            "ktx-async",
            extKeys = listOf("ktxAsyncVersion")
        )
    ),
    KTX_ASLEY(
        Library(
            "KTX ashley",
            "io.github.libktx",
            "ktx-ashley",
            extKeys = listOf("ktxAshleyVersion")
        )
    ),
    KTX_BOX2D(
        Library(
            "KTX box2d",
            "io.github.libktx",
            "ktx-box2d",
            extKeys = listOf("ktxBox2dVersion")
        )
    ),
    KTX_COLLECTIONS(
        Library(
            "KTX collections",
            "io.github.libktx",
            "ktx-collections",
            extKeys = listOf("ktxCollectionsVersion")
        )
    ),
    KTX_FREETYPE(
        Library(
            "KTX freetype",
            "io.github.libktx",
            "ktx-freetype",
            extKeys = listOf("ktxFreetypeVersion")
        )
    ),
    KTX_FREETYPE_ASYNC(
        Library(
            "KTX freetype-async",
            "io.github.libktx",
            "ktx-freetype-async",
            extKeys = listOf("ktxFreetypeAsyncVersion")
        )
    ),
    KTX_GRAPHCIS(
        Library(
            "KTX graphics",
            "io.github.libktx",
            "ktx-graphics",
            extKeys = listOf("ktxGraphicsVersion")
        )
    ),
    KTX_I18N(
        Library(
            "KTX i18n",
            "io.github.libktx",
            "ktx-i18n",
            extKeys = listOf("ktxI18nVersion")
        )
    ),
    KTX_INJECT(
        Library(
            "KTX inject",
            "io.github.libktx",
            "ktx-inject",
            extKeys = listOf("ktxInjectVersion")
        )
    ),
    KTX_JSON(
        Library(
            "KTX json",
            "io.github.libktx",
            "ktx-json",
            extKeys = listOf("ktxJsonVersion")
        )
    ),
    KTX_LOG(
        Library(
            "KTX log",
            "io.github.libktx",
            "ktx-log",
            extKeys = listOf("ktxLogVersion")
        )
    ),
    KTX_MATH(
        Library(
            "KTX math",
            "io.github.libktx",
            "ktx-math",
            extKeys = listOf("ktxMathVersion")
        )
    ),
    KTX_PREFERENCES(
        Library(
            "KTX preferences",
            "io.github.libktx",
            "ktx-preferences",
            extKeys = listOf("ktxPreferencesVersion")
        )
    ),
    KTX_SCENE2D(
        Library(
            "KTX scene2d",
            "io.github.libktx",
            "ktx-scene2d",
            extKeys = listOf("ktxScene2DVersion")
        )
    ),
    KTX_STYLE(
        Library(
            "KTX style",
            "io.github.libktx",
            "ktx-style",
            extKeys = listOf("ktxStyleVersion")
        )
    ),
    KTX_TILED(
        Library(
            "KTX tiled",
            "io.github.libktx",
            "ktx-tiled",
            extKeys = listOf("ktxTiledVersion")
        )
    ),
    KTX_VIS(
        Library(
            "KTX vis",
            "io.github.libktx",
            "ktx-vis",
            extKeys = listOf("ktxVisVersion")
        )
    ),
    KTX_VIS_STYLE(
        Library(
            "KTX vis-style",
            "io.github.libktx",
            "ktx-vis-style",
            extKeys = listOf("ktxVisStyleVersion")
        )
    ),
    LIBGDX_ANNOTATIONS(
        Library(
            "LibGDX Annotations",
            "com.gmail.blueboxware",
            "libgdxpluginannotations",
            repository = Repository.JCENTER
        )
    ),
    LIBGDXUTILS(
        Library(
            "libgdx-utils",
            "net.dermetfan.libgdx-utils",
            "libgdx-utils",
            extKeys = listOf(
                "gdxUtilsVersion"
            )
        )
    ),
    LIBGDXUTILS_BOX2D(
        Library(
            "libgdx-utils-box2d",
            "net.dermetfan.libgdx-utils",
            "libgdx-utils-box2d"
        )
    ),
    LML(
        Library(
            "gdx-lml",
            "com.github.czyzby",
            "gdx-lml",
            extKeys = listOf("lmlVersion")
        )
    ),
    LMLVIS(
        Library(
            "gdx-lml-vis",
            "com.github.czyzby",
            "gdx-lml-vis",
            extKeys = listOf("lmlVisVersion")
        )
    ),
    MAKE_SOME_NOISE(
        Library(
            "make-some-noise",
            "com.github.tommyettinger",
            "make_some_noise",
            extKeys = listOf("makeSomeNoiseVersion")
        )
    ),
    NOISE4J(
        Library(
            "Noise4J",
            "com.github.czyzby",
            "noise4j",
            extKeys = listOf("noise4jVersion")
        )
    ),
    OVERLAP2D(
        Library(
            "Overlap2D",
            "com.underwaterapps.overlap2druntime",
            "overlap2d-runtime-libgdx",
            extKeys = listOf("overlap2dVersion")
        )
    ),
    REGEXODUS(
        Library(
            "RegExodus",
            "com.github.tommyettinger",
            "regexodus",
            extKeys = listOf("regExodusVersion")
        )
    ),
    SHAPE_DRAWER(
        Library(
            "Shape Drawer",
            "space.earlygrey",
            "shapedrawer",
            extKeys = listOf("shapeDrawerVersion"),
            repository = Repository.JITPACK
        )
    ),
    SPINE(
        Library(
            "spine-libgdx",
            "com.esotericsoftware.spine",
            "spine-libgdx",
            extKeys = listOf("spineRuntimeVersion")
        )
    ),
    SQUIDLIB(
        Library(
            "SquidLib",
            "com.squidpony",
            "squidlib",
            extKeys = listOf("squidLibVersion")
        )
    ),
    SQUIDLIB_EXTRA(
        Library(
            "SquidLib Extra",
            "com.squidpony",
            "squidlib-extra",
            extKeys = listOf("squidLibExtraVersion")
        )
    ),
    SQUIDLIB_UTIL(
        Library(
            "SquidLib Util",
            "com.squidpony",
            "squidlib-util",
            extKeys = listOf("squidLibUtilVersion")
        )
    ),
    TEN_PATCH(
        Library(
            "TenPatch",
            "com.github.raeleus.TenPatch",
            "tenpatch",
            extKeys = listOf("tenPatchVersion"),
            repository = Repository.JITPACK
        )
    ),
    TYPING_LABEL(
        Library(
            "TypingLabel",
            "com.rafaskoberg.gdx",
            "typing-label",
            extKeys = listOf("typingLabelVersion")
        )
    ),
    VISUI(
        Library(
            "VisUI",
            "com.kotcrab.vis",
            "vis-ui",
            extKeys = listOf("visUiVersion")
        )
    ),
    VISRUNTIME(
        Library(
            "Vis Runtime",
            "com.kotcrab.vis",
            "vis-runtime",
            extKeys = listOf("visRuntimeVersion")
        )
    ),
    WEBSOCKET(
        Library(
            "Web Sockets",
            "com.github.czyzby",
            "gdx-websocket",
            extKeys = listOf("websocketVersion")
        )
    ),
    WEBSOCKET_SERIALIZATION(
        Library(
            "Web Sockets Serialization",
            "com.github.czyzby",
            "gdx-websocket-serialization",
            extKeys = listOf("websocketSerializationVersion")
        )
    ), ;

    override fun toString() = library.name

    companion object {

        fun listOfCheckedLibraries() =
            entries
                .map { it.library.name }
                .sortedBy { it.lowercase(Locale.getDefault()) }
                .joinToString(", ")


    }

}
