[![Current version](https://img.shields.io/github/release/BlueBoxware/LibGDXPlugin.svg)](https://github.com/BlueBoxWare/LibGDXPlugin/releases/latest)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/8509-libgdx-inspections.svg)](https://plugins.jetbrains.com/plugin/8509-libgdx-inspections)

This unofficial plugin adds a number of [LibGDX](https://libgdx.badlogicgames.com/) related features and tools to
[IntelliJ](https://www.jetbrains.com/idea/) and [Android Studio](https://developer.android.com/studio/index.html).

- [Installation](#installation)
- [Features](#features)
  - [Inspections](#inspections)
  - [Color previews](#color-previews)
  - [Skin JSON support](#skin-json-support)
  - [Atlas file support](#atlas-file-support)
  - [Bitmap Font file support](#bitmap-font-file-support)
  - [Skin resources and Atlas region names in Java and Kotlin code](#skin-resources-and-atlas-region-names-in-java-and-kotlin-code)

# Installation
In IntelliJ or Android Studio go to *Settings* -> *Plugins* -> *Browse repositories...* and search for "LibGDX".

Alternatively: download the zip [from the Jetbrains Plugin Repository](https://plugins.jetbrains.com/plugin/8509),
go to *Settings* -> *Plugins* -> *Install plugin from disk* and select the zip you downloaded.

This plugin needs a recent version of the official [Kotlin plugin](https://plugins.jetbrains.com/plugin/6954) (even if
you only use Java), so please make sure to enable (and if necessary update) that plugin first. To update the Kotlin plugin
to the newest version, go to: *Tools* -> *Kotlin* -> *Configure Kotlin Plugin Updates* -> *Check for updates now*.

# Features

## Inspections
LibGDXPlugin adds several inspections, which look for possible issues in a project. Code inspections support both Java and
[Kotlin](https://kotlinlang.org/). To disable or enable inspections go to *Settings* -> *Editor* -> *Inspections* -> *LibGDX*.
The following inspections are included:

* Use of profiling code, like FPSLogger or GLProfiler
* Setting an overly verbose log level
* Causing flushing of a batch or renderer from inside a loop
* Missing flush() after changing Preferences
* Use of some commonly known test ids or dummy ids (ex. AdMob and MoPub)
* Use of ShapeRenderer with a release of LibGDX older than 1.9.2 (It crashes on 64bit devices. See [Issue 3790](https://github.com/libgdx/libgdx/issues/3790)) \[1]
* Use of static resources (more info on the use of statics: [here](http://bitiotic.com/blog/2013/05/23/libgdx-and-android-application-lifecycle/) and [here](http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=22358))
* Use of non-reentrant iterator methods of LibGDX collection classes
* Missing OpenGL declaration in AndroidManifest.xml \[1]
* Invalid property keys for I18NBundle.get() and I18NBundle.format()
* Missing WRITE_EXTERNAL_STORAGE permission in AndroidManifest.xml when using external files
* Using outdated versions of LibGDX and related libraries \[1] \[2]
* Declaring a combination of minSdkVersion, maxSdkVersion, targetSdkVersion and &lt;support-screens&gt; which excludes the App from being listed as "Designed for Tablets" in the Google Play Store \[1]

\[1]: These inspections assume the project uses a fairly standard setup, like those created by `gdx-setup` and [`gdx-setup`](https://github.com/czyzby/gdx-setup).

## Color previews
When using a LibGDX color in Java or Kotlin code (e.g. `Color.BLUE` or `Color.valueOf("#0000ff")`) a preview of the the color is shown in the left gutter.
Color previews are also shown in the editor when editing Skin files and in the Debug Tool Window.

To disable color previews, go to *Settings* -> *Editor* -> *LibGDXPlugin*.

## Skin JSON support
Files with the extension `.skin` are treated as Skin JSON files. For files with the extension `.json` which look like Skin files, you are asked
whether they should be treated as Skin files (this can be turned off in the settings). You can also mark and unmark files as Skin files using the
context menu of a file.

<img align="right" src="/images/skinCompletion.gif" width="450">

For files which are marked as Skin files, the plugin provides additional Skin related support, including
* Syntax highlighting (can be configured using *Settings* -> *Editor* -> *Colors & Fonts* -> *LibGDX Skin*)
* Color previews in the left gutter. Click on a color preview to open a color selector dialog
* [Structure View](https://www.jetbrains.com/help/idea/2016.2/navigating-with-structure-views.html)
* Code completion for class names, property names, property values and, if an .atlas file with the same name as the Skin and in the same directory
exists, drawable/texture names
* Folding
* Formatting/Code Style (Code Style can be configured using *Settings* -> *Editor* -> *Code Style* -> *LibGDX Skin*)
* Warnings when using classes which don't exist, using inner classes which are not static, using properties which don't correspond to a field in a
given class or using malformed color strings
* Find usages of a defined resources within the Skin file
* *Crtl-B* (*⌘+B*): navigate from the usage of a resource to it's definition
* *Crtl-B* (*⌘+B*) with the caret on a class or field name: navigate to the source of the class or field
* *Crtl-B* (*⌘+B*) on a bitmap font name: navigate to the corresponding bitmap font file
* Renaming a resource with *Shift-F6* also renames it's usages in the Skin files \[1]
* (Un)commenting blocks of code with *Ctrl-/*
* [Smart Enter](https://www.jetbrains.com/help/idea/2016.3/completing-statements.html)
* With *Shift* pressed, hover over a Drawable/Texture name to view a preview of the Drawable

\[1]: Usages of the resource in Java/Kotlin code are not automatically renamed, expect when using the `@GDXAssets`
annotation (see below)

## Atlas file support

<img align="right" src="/images/atlasFile.png" width="450">

Files with an `.atlas` or `.pack` extension are treated as Texture Atlas packfiles.

Atlas file support includes:
* Highlighting
* [Structure View](https://www.jetbrains.com/help/idea/2016.2/navigating-with-structure-views.html)
* Folding
* With *Shift* pressed, hover over a Region to view a preview of the image

## Bitmap Font file support

Files with a `.fnt` extension are treated as Bitmap Font Files, with:
* Highlighting
* Structure View
* Folding

## Skin resources and Atlas region names in Java and Kotlin code

To get code completion, Go to Definition, Find Usages, Rename Refactoring, Diagnostics and Image previews (with *Shift* pressed, hover over a Drawable name to get a preview) for:
* Skin resource names
* Region names from Atlas files in Skin.get*() and TextureAtlas.get*()
* Property keys in I18NBundle.get() and I18NBundle.format()

and related methods, use the `@GDXAssets` annotation to tell LibGDXPlugin which files to use.

First add the annotation to your build. In `build.gradle`:

```gradle

repositories {
    // ...
    jcenter()
}

dependencies {
    // ...
    compile 'com.gmail.blueboxware:libgdxpluginannotations:1.13'
}

```

Then annotate Java fields and Kotlin properties where appropriate. Specify file names **relative to the Project Root**!

**Java:**
```java
        @GDXAssets(skinFiles = {"android/assets/ui.skin"})
        // or Windows style: @GDXAssets(skinFiles = {"android\\assets\\ui.skin"})
        Skin uiSkin = new Skin(Gdx.files.internal("ui.skin"));

        @GDXAssets(
                skinFiles = {"assets/default.skin", "assets/main.skin"},
                atlasFiles = {"assets/images.atlas"}
        )
        Skin skin = new Skin();
        skin.load(DEFAULT_SKIN);
        skin.load(MAIN_SKIN);
        skin.addRegions(ATLAS);

        @GDXAssets(atlasFiles = {"assets/images/images.pack"})
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/images.pack"));

        @GDXAssets(propertiesFile = {"assets/18n/Messages.properties"}) // Specify the base file, including .properties extension
        I18NBundle bundle = I18NBundle.createBundle(Gdx.files.internal("i18n/Messages"));
```

**Kotlin:**
```kotlin
    @GDXAssets(skinFiles = ["android/assets/ui.skin"])
    // or Windows style: @GDXAssets(skinFiles = ["android\\assets\\ui.skin"])
    val uiSkin = Skin(Gdx.files.internal("ui.skin"))

    @GDXAssets(
            skinFiles = ["assets/default.skin", "assets/main.skin"],
            atlasFiles = ["assets/textures.atlas"]
    )
    val skin = Skin()
    skin.load(DEFAULT_SKIN)
    skin.load(MAIN_SKIN)
    skin.addRegions(ATLAS)

    @GDXAssets(atlasFiles = ["assets/images/images.pack"])
    val atlas: TextureAtlas = TextureAtlas(Gdx.files.internal("images/images.pack"))
```

**NOTES**
* Specify file names **relative to the Project Root**!
* Multiple files can be specified for both the `skinFiles`, `atlasFiles` and `propertiesFiles` parameters
* When *no* atlasFiles are specified and a file with the same name as the specified Skin file and the
".atlas" extension exist, that file is used as Atlas file (just like the Skin class itself does). This also
works if you specify multiple Skin files.
* Go To Definition and Find Usages are only available if the specified files are registered as Skin or Atlas file, not
when they are registered as JSON or Plain Text files.