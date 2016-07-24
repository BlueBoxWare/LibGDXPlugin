# LibGDXPlugin
This unofficial plugin adds a number of LibGDX related code inspections for IntelliJ. Java and Kotlin are supported.

Currently available inspections:

* Missing OpenGL declaration in AndroidManifest.xml
* Using outdated versions of LibGDX and related libraries
* Missing flush() after changing Preferences
* Use of profiling code, like FPSLogger or GLProfiler
* Setting an overly verbose log level
* Causing flushing of a batch or renderer from inside a loop
* Use of AdMob test ids
* Use of ShapeRenderer with a release of LibGDX older than 1.9.2
* Use of static resources
* Use of non-reentrant iterator methods of LibGDX collection classes
* Declaring a combination of minSdkVersion, maxSdkVersion, targetSdkVersion and &lt;support-screens&gt; which excludes the App from being listed as "Designed for Tablets" in the Google Play Store
