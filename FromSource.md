Running and building from source
================================

### Prepare IntelliJ
1. Download and install IntelliJ
2. Enable the Gradle plugin
3. Install and enable the Kotlin plugin
4. Install and enable the "Plugin DevKit" plugin
5. Install and enable the "Grammar-Kit" plugin

### Get the source
6. `git clone https://github.com/BlueBoxWare/LibGDXPlugin.git`

### Import the project
7. Import the project: *"File" -> "New" -> "Project from Existing Sources"* and select the `build.gradle` file from the project.
    1. In the import dialog select "_**Use gradle wrapper task configuration**_"
    2. Wait while Gradle and the [gradle-intellij-plugin](https://github.com/JetBrains/gradle-intellij-plugin) work their magic. This can take a while..
    3. When you get a popup saying "_Frameworks detected: Android framework is detected in the project_", do not configure it but ignore and close the popup
    4. When you get a popup saying "_The modules below are not imported from Gradle anymore_" leave the item(s) selected to remove them and click "Oke"
8. Open _gradle.properties_ and make sure _isAndroidStudio_ is set to **false**: `isAndroidStudio=false`

### Running
To run IntelliJ with the plugin enabled from sources, start the "RunIde" gradle task: open the Gradle window, choose "Tasks" -> "intellij" and double click on "RunIde".

### Building
Make sure the plugin works when you run it as described above.<br>
To build the plugin, run the "buildPlugin" gradle task. This creates a zip file in the "build/distributions" directory. You can install the plugin in IntelliJ or 
Android Studio by choosing "Install plugin from disk" in the Plugin Settings.

### Running the tests
TBD