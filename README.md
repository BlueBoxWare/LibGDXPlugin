# LibGDXPlugin
This unofficial plugin adds a number of LibGDX related tools and features to [IntelliJ](https://www.jetbrains.com/idea/) and
[Android Studio](https://developer.android.com/studio/index.html). You can install the plugin 
[from the Jetbrains Plugin Repository](https://plugins.jetbrains.com/plugin/8509). 

This plugin needs a recent version of the official [Kotlin plugin](https://plugins.jetbrains.com/plugin/6954) to be installed (even if
you only use Java), so install and/or enable that plugin first. 

## Features

### Inspections
LibGDXPlugin adds several inspections, which look for possible issues in a project. 
Code inspections support both Java and [Kotlin](https://kotlinlang.org/). 
To disable or enable inspections go to *Settings* -> *Editor* -> *Inspections* -> *LibGDX*. 
The following inspections are included:

* Use of profiling code, like FPSLogger or GLProfiler
* Setting an overly verbose log level
* Causing flushing of a batch or renderer from inside a loop
* Missing flush() after changing Preferences
* Use of AdMob test ids
* Use of ShapeRenderer with a release of LibGDX older than 1.9.2 (It crashes on 64bit devices. See [Issue 3790](https://github.com/libgdx/libgdx/issues/3790)) \[1]
* Use of static resources (more info on the use of statics: [here](http://bitiotic.com/blog/2013/05/23/libgdx-and-android-application-lifecycle/) and [here](http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=22358))
* Use of non-reentrant iterator methods of LibGDX collection classes
* Missing OpenGL declaration in AndroidManifest.xml \[1]
* Using outdated versions of LibGDX and related libraries \[1]
* Declaring a combination of minSdkVersion, maxSdkVersion, targetSdkVersion and &lt;support-screens&gt; which excludes the App from being listed as "Designed for Tablets" in the Google Play Store \[1]

\[1]: These inspections assume the project uses a fairly standard setup, as created by `gdx-setup`. 

### Color previews
When using a LibGDX color in Java or Kotlin code (e.g. `Color.BLUE` or `Color.valueOf("#0000ff")`) a preview of the the color is shown in the left gutter.

To disable color previews, go to *Settings* -> *Editor* -> *LibGDXPlugin*.

### Skin JSON support
Files with the extension `.skin` are treated as Skin JSON files. For files with the extension `.json` which look like Skin files, you are asked
whether they should be treated as Skin files (can be turned of in the settings). You can also mark and unmark files as Skin files using the context menu of a file.

<img align="right" src="/images/skinCompletion.gif" width="400">

Skin file support includes:
* Syntax highlighting (can be configured using *Settings* -> *Editor* -> *Colors & Fonts* -> *LibGDX Skin*)
* Color previews in the left gutter. Click on a color preview to open a color selector dialog
* [Structure View](https://www.jetbrains.com/help/idea/2016.2/navigating-with-structure-views.html)
* Code completion for class names, property names, property values and, if an .atlas file with the same name as the Skin and in the same directory
exists, drawable/texture names
* Folding
* Formatting/Code Style (Code Style can be configured using *Settings* -> *Editor* -> *Code Style* -> *LibGDX Skin*)
* Warnings when using classes which don't exist, using properties which don't correspond to a field in a given class or using malformed color strings
* Find usages of a defined resources within the Skin file \[1]
* *Crtl-B*: Jump from the usage of a resource to it's definition \[1]
* *Ctrl-B* with the caret on a class name jumps to the source of the class and *Ctrl-B* with the caret on a property name jumps to the corresponding field
* *Ctrl-B* on a bitmap font name jumps to the corresponding bitmap font file
* Renaming a resource with *Shift-F6* also renames it's usages in the Skin files \[2]
* (Un)commenting blocks of code with *Ctrl-/*
* [Smart Enter](https://www.jetbrains.com/help/idea/2016.3/completing-statements.html)

\[1]: Navigating between definition and usages of a resource in Java/Kotlin code is currently not supported

\[2]: Usages of the resource in Java/Kotlin code are not automatically renamed

### Atlas file support

<img align="right" src="/images/atlasFile.png">

Files with a `.atlas` or `.pack` extension are treated as Texture Atlas packfiles.

Atlas file support includes:
* Highlighting
* [Structure View](https://www.jetbrains.com/help/idea/2016.2/navigating-with-structure-views.html)
* Folding

