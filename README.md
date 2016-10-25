# LibGDXPlugin
This unofficial plugin adds a number of LibGDX related (mostly Android centered) code inspections for IntelliJ and Android Studio. Java and Kotlin are supported. You can install the plugin [from the Jetbrains Plugin Repository](https://plugins.jetbrains.com/plugin/8509). 

Currently available inspections:

* Missing OpenGL declaration in AndroidManifest.xml
* Using outdated versions of LibGDX and related libraries
* Missing flush() after changing Preferences
* Use of profiling code, like FPSLogger or GLProfiler
* Setting an overly verbose log level
* Causing flushing of a batch or renderer from inside a loop
* Use of AdMob test ids
* Use of ShapeRenderer with a release of LibGDX older than 1.9.2 (It crashes on 64bit devices. [Issue 3790](https://github.com/libgdx/libgdx/issues/3790))
* Use of static resources (more info on the use of statics: [1](http://bitiotic.com/blog/2013/05/23/libgdx-and-android-application-lifecycle/), [2](http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=22358))
* Use of non-reentrant iterator methods of LibGDX collection classes
* Declaring a combination of minSdkVersion, maxSdkVersion, targetSdkVersion and &lt;support-screens&gt; which excludes the App from being listed as "Designed for Tablets" in the Google Play Store

Other:

* Adds preview colors to the left gutter of the editor for lines which contain a color string (e.g. "#ff00ff") or a call to a constructor of LibGDX Color class with constants as arguments. Can be enabled/disabled by going to Settings->Editor->LibGDXPlugin.
* Adds a file-template to the File->New menu to create a com.badlogic.gdx.Screen


