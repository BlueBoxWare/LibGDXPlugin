### 1.24.3
* Compatibility with upcoming versions of IntelliJ.

### 1.24.2
* Compatibility with IntelliJ 2022.3.1.

### 1.24.1
* Use documentation popup for image previews instead of Shift + hover.
* Compatibility with recent versions of IntelliJ.

### 1.24
* Support for the [new Atlas file format](https://github.com/libgdx/libgdx/commit/a4a0240e2db47d7273797eaf371133f84749f823).

### 1.23.6
* Improvements in Find Usages and type checking for Skin files.
* Fix "Slow operations are prohibited on EDT" error in "Find Usages".

### 1.23.5
* Significant perfomance improvements in Skin handling and related functionality ([#33](https://github.com/BlueBoxWare/LibGDXPlugin/issues/33)).

### 1.23.4
* Fix perfomance issue with Skin files ([#33](https://github.com/BlueBoxWare/LibGDXPlugin/issues/33))

### 1.23.3
* Fix "Slow operations are prohibited on EDT" error with IntelliJ 2021.2.

### 1.23.2 
* Fix IllegalAccessError with IntelliJ 2021.1.*.

### 1.23
* Some Suppression ids have been changed for consistency. See [Inspections.md](https://github.com/BlueBoxWare/LibGDXPlugin/Inspections.md) for an up to date list.
* Reworking of JSON handling, fixing several bugs (like [#26](https://github.com/BlueBoxWare/LibGDXPlugin/issues/26)).
* [#27]: Display a warning when *.gwt.xml files are changed in a html module that a full rebuild is necessary.
* JSON: highlight usages of the same key in a file, find usages of a key in a file, rename all usages of a key.

### 1.22.1
* Bundle kotlin-reflect dependency.

### 1.22
* Compatibility with Kotlin plugin 1.4.20+ ([#23](https://github.com/BlueBoxWare/LibGDXPlugin/issues/23))
* Build for Java target 1.8 (fixes compatibility with Android Studio)

### 1.21
* Changed plugin name to libGDX ([#22](https://github.com/BlueBoxWare/LibGDXPlugin/issues/22)).
* Improvements and additions to the outdated library versions inspections.

### 1.20.2
* Compatibility with IntelliJ 2020.1.

### 1.20.1
* Fix NPE when running with IntelliJ 2019.2 EAP.
            
### 1.20
* Skin files now use the standard IntelliJ method of suppressing warnings, with `//noinspection` comments.
* libGDX JSON: allow suppression of warnings.
* Skin files & libGDX JSON: spell checking.
* Skin files & libGDX JSON: breadcrumbs.
* libGDX JSON: move array element foward/backward intentions.
  
### 1.19
* Support for libGDX style JSON ([#19](https://github.com/BlueBoxWare/LibGDXPlugin/issues/19)).
 In the project panel context menu use <i>Mark as libGDX style JSON</i> to have a file treated as libGDX JSON.

### 1.18
* Support for com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator in Skin files ([#18](https://github.com/BlueBoxWare/LibGDXPlugin/issues/18)).
* Minor fixes

### 1.17.2
* Fix issue [#17](https://github.com/BlueBoxWare/LibGDXPlugin/issues/17).

### 1.17.1
* Fix issues with IntelliJ 2018.3 EAP

### 1.17
* Support for com.badlogic.gdx.graphics.Colors
* Find Usages for tags in @GDXTag annotation
* Unused tag inspection for @GDXTag annotation
* Show color previews in decompiled classes too
* Always show completion popup when there is something useful to show
* Miscellaneous small improvements

### 1.16.1
* Fix non existing resource inspection: it should only look at annotated elements
        
### 1.16
* Inspection to highlight references in Java/Kotlin to resources which don't exist. @GDXAssets annotated
fields and variables only.
* Warn about outdated libraries in Gradle Kotlin DSL too
* Performance improvements
* Skins: Inspection to inform about the ability to use short class names instead of qualified names when
using libGDX >= 1.9.9
* Skins: Inspection to highlight usage of deprecated classes and fields
* Skins: Quickfix to create resources which don't exist
* Skins: Use the @GDXTag annotation to inform the plugin about custom short names for classes
* Skins: Intentions to convert color resources to hex or to float components
* Skins: Create new color wizard (`Code` -> `Generate` or `Alt-Insert`, `Command-N`)

### 1.15.2
* Fix Settings pane ([#14](https://github.com/BlueBoxWare/LibGDXPlugin/issues/14)).
* Add setting to enable/disable color previews in Skins

### 1.15.1
* Don't crash on 'invalid' color values ([#13](https://github.com/BlueBoxWare/LibGDXPlugin/issues/13)).
* Skins: don't treat parent property as special when using a libGDX version below 1.9.9

### 1.15
* Skins: support tagged classes and cascading styles (libGDX 1.9.9, [#12](https://github.com/BlueBoxWare/LibGDXPlugin/issues/12)).
* Minor fixes and improvements

### 1.14
* Warning when @GDXAsset annotation is used on an invalid target
* @GDXAsset related functionality now works correctly with array literals in the annotation (available
since Kotlin 1.2)
* Minor fixes

### 1.13
* Completion for `I18NBundle.get()` and `I18NBundle.format()` arguments
* Improved Go to definition, Find Usages and renaming support for properties keys
* Invalid property key inspection for `I18NBundle.get()` and `I18NBundle.format()`

### 1.12
* New inspection: missing `WRITE_EXTERNAL_STORAGE` permission
* Skin files: improved autocompletion, refactoring support and string handling
* Bugfixes

### 1.11
* Skin files:
    * Allow suppression of warnings for specific objects or entire file
    * Added quickfixes to suppress warnings
    * Added a number of inspections
    * Smart closing quotes insertion and removal
* Bugfixes
* Previews for Textures/Drawables: with `SHIFT` pressed, hover over a region in an Atlas files, a Drawable
name in a Skin file or Drawable name in Skin methods of a properly annotated Skin object.
* Color previews in the debug view

### 1.10
* Proper support for arrays and nested objects in Skin files
* Fixed Structure View not updating when changing files

### 1.9
* Support for Completion, Go to Definition and Find Usages of resources from Skin files and Atlas files in
Java and Kotlin code. Use the @LibGDXAssets annotation to specify the files to use. See the README for usage
instructions.
* Atlas files: allow spaces in region names
* Misc. bug fixes
