[![Current version](https://img.shields.io/github/release/BlueBoxware/LibGDXPlugin.svg)](https://github.com/BlueBoxWare/LibGDXPlugin/releases/latest)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/8509-libgdx-inspections.svg)](https://plugins.jetbrains.com/plugin/8509-libgdx-inspections)

This unofficial plugin adds a number of [libGDX](https://libgdx.badlogicgames.com/) related features and tools to
[IntelliJ](https://www.jetbrains.com/idea/) and [Android Studio](https://developer.android.com/studio/index.html).

<!-- toc -->
- __[Installation](#installation)__
- __[Features](#features)__
  - __[Inspections](#inspections)__
  - __[Color previews](#color-previews)__
  - __[Skin JSON support](#skin-json-support)__
  - __[JSON support](#json-support)__
  - __[Atlas file support](#atlas-file-support)__
  - __[Bitmap Font file support](#bitmap-font-file-support)__
  - __[Skin resources and Atlas region names in Java and Kotlin code](#skin-resources-and-atlas-region-names-in-java-and-kotlin-code)__
    - __[@GDXTag and short names in Skins](#gdxtag-and-short-names-in-skins)__
<!-- /toc -->

# Installation
In IntelliJ or Android Studio go to *Settings* -> *Plugins* -> *Browse repositories...* and search for "libGDX".

Alternatively: download the zip [from the Jetbrains Plugin Repository](https://plugins.jetbrains.com/plugin/8509),
go to *Settings* -> *Plugins* -> *Install plugin from disk* and select the zip you downloaded.

This plugin needs a recent version of the official [Kotlin plugin](https://plugins.jetbrains.com/plugin/6954) (even if
you only use Java), so please make sure to enable (and if necessary update) that plugin first. To update the Kotlin plugin
to the newest version, go to: *Tools* -> *Kotlin* -> *Configure Kotlin Plugin Updates* -> *Check for updates now*.

# Features

## Inspections
libGDXPlugin adds several inspections, which look for possible issues in a project. Code inspections support both Java and
[Kotlin](https://kotlinlang.org/). To disable or enable inspections go to *Settings* -> *Editor* -> *Inspections* -> *libGDX*.
See [Inspections.md](Inspections.md]) for an up to date list.

## Color previews
When using a libGDX color in Java or Kotlin code (e.g. `Color.BLUE` or `Color.valueOf("#0000ff")`) a preview of the the color is shown in the left gutter.
Color previews are also shown in the editor when editing Skin files and in the Debug Tool Window.

To disable color previews, go to *Settings* -> *Editor* -> *libGDXPlugin*.

## Skin JSON support
Files with the extension `.skin` are treated as Skin JSON files. For files with the extension `.json` which look like Skin files, you are asked
whether they should be treated as Skin files (this can be turned off in the settings). You can also mark and unmark files as Skin files using the
context menu of a file.

<img align="right" src="/images/skinCompletion.gif" width="450">

For files which are marked as Skin files, the plugin provides additional Skin related support, including
* Syntax highlighting (can be configured using *Settings* -> *Editor* -> *Colors & Fonts* -> *libGDX Skin*)
* Color previews in the left gutter. Click on a color preview to open a color selector dialog
* [Structure View](https://www.jetbrains.com/help/idea/2016.2/navigating-with-structure-views.html)
* Code completion for class names, property names, property values and, if an .atlas file with the same name as the Skin and in the same directory
exists, drawable/texture names
* Folding
* Formatting/Code Style (Code Style can be configured using *Settings* -> *Editor* -> *Code Style* -> *libGDX Skin*)
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
* Use *Code* -> *Generate* or *Alt-Ins* (*Cmd-N*) to create a new color using a color pick dialog
* QuickFixes to create missing resources and convert colors between hex and floats
* Several inspections to highlight problems and possible issues
* [Breadcrumbs](https://www.jetbrains.com/help/idea/settings-editor-breadcrumbs.html)

\[1]: Usages of the resource in Java/Kotlin code are not automatically renamed, expect when using the `@GDXAssets`
annotation (see below)

## JSON support
IntelliJ doesn't work well with libGDX-style JSON, which is more forgiving when it comes to things like unquoted strings and missing comma's. 
libGDXPlugin adds support for libGDX's custom JSON with the usual niceties of syntax coloring, completion, folding, etc.

To have libGDXPlugin treat a JSON file as a libGDX-style JSON file: in the Project Window, open the context menu for the file 
and select `Mark as libGDX style JSON`.

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

To get code completion, Go to Definition, Find Usages, Rename Refactoring, Diagnostics and Image previews for:
* Skin resource names
* Region names from Atlas files in Skin.get*() and TextureAtlas.get*()
* Property keys in I18NBundle.get() and I18NBundle.format()

and related methods, use the `@GDXAssets` annotation to tell libGDXPlugin which files to use.

First add the annotation to your build. In `build.gradle`:

```gradle

repositories {
    // ...
    jcenter()
}

dependencies {
    // ...
    compile 'com.gmail.blueboxware:libgdxpluginannotations:1.16'
}

```

Then annotate Java fields and Kotlin properties where appropriate. Specify file names **relative to the Project Root**.

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
* Specify file names relative to the Project Root
* Multiple files can be specified for both the `skinFiles`, `atlasFiles` and `propertiesFiles` parameters
* When *no* atlasFiles are specified and a file with the same name as the specified Skin file and the
".atlas" extension exist, that file is used as Atlas file (just like the Skin class itself does). This also
works if you specify multiple Skin files.
* Go To Definition and Find Usages are only available if the specified files are registered as Skin or Atlas file, not
when they are registered as JSON or Plain Text files.

### @GDXTag and short names in skins
Since version 1.9.9 libGDX skins support tagged classes: the ability to use short names for names of classes
in Skin files. In addition to the standard, "built-in" short class names it is also possible to define custom
short names for your own classes by overriding `Skin.getJsonLoader()` and calling `Json.addClassTag()`.

libGDXPlugin understands the default short names. It also tries to determine any custom short names by looking
for calls to `addClassTag()`, but there is only so much it can do.

To explicitly tell the plugin to recognize one (or more) short names for one of your own classes, you can
use the `@GDXTag` annotation on that class.

```java
package com.something.ui;

@GDXTag({"Widget"})
class MyCustomWidget {
  // ...
}
```

After this, the plugin will recognize `Widget` as a short name for `com.something.ui.MyCustomWidget` in
Skin files. It is of course still up to you to make libGDX recognize this short name by subclassing Skin or
by some other means.

Note that at the moment there is no way to tell the plugin a short name is only valid in specific Skin files,
instead of all Skin files.
