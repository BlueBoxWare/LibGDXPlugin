| Suppression ID | Name | Description |
|---|---|---|
| GDXAbbrClass | Use short class name | Notes when a short class name can be used for a standard scene2d.ui class instead of the full class name (libGDX >= 1.9.9). |
| GDXDuplicateProperty | Duplicate properties in Skin files | Looks for duplicate properties in Skin files. |
| GDXDuplicateResource | Duplicate resource names in Skin files | Looks for duplicate resource name in Skin files. |
| GDXGradlePropertiesTestId | Test IDs or dummy IDs in gradle.properties | Looks for the use of some known test IDs and dummy IDs. |
| GDXGradleTestId | Test IDs or dummy IDs in build.gradle | Looks for the use of some known test IDs and dummy IDs. |
| GDXInvalidEscape | Invalid escape sequence | Marks invalid escape sequences in libGDX style JSON files. |
| GDXJavaAssetsFileError | @GDXAssets problem | Checks for problems related to @GDXAssets annotations |
| GDXJavaFlushInsideLoop | Flushing a batch inside a loop | Looks for the possibility of a flush of a batch or renderer occuring inside a loop, either directly or indirectly. <br />For performance reasons care should be taken to not cause unnecessary flushes, and to limit the number of flushes per frame as much as possible. <br />Note that calling SpriteBatch.draw(..) with a different texture also causes a flush, which is not detected by this inspection. Use a <a href="https://github.com/libgdx/libgdx/wiki/Texture-packer">Texture Atlas</a> instead of many different textures. |
| GDXJavaInvalidPropertyKey | Property key does not exist | Looks for the use of property keys which do not exist. |
| GDXJavaLogLevel | Overly verbose log level | Looks for calls to Gdx.app.setLogLevel() setting the log level to LOG_INFO or LOG_VERBOSE, which while useful for debugging should propably not be used in production builds. |
| GDXJavaMissingFlush | Missing flush on Preferences | Call Preferences.flush() to make sure changes to preferences are persisted. |
| GDXJavaNonExistingAsset | Resource doesn't exist | Looks in Java and Kotlin code for usages of resources which don't exist. @GDXAssets annotated elements only. |
| GDXJavaProfilingCode | Profiling code | Looks for profiling code, which should be disabled before release. |
| GDXJavaStaticResource | Static resource | Don't make resources static, unless you take care to properly manage them. Static resources can cause problems on Android, because the life-cycle of a static variable is not necessarily the same as the life-cycle of your application. |
| GDXJavaTestId | Test IDs or dummy IDs | Looks for the use of some known test IDs and dummy IDs. |
| GDXJavaUnsafeIterator | Use of non-reentrant iterator method | If Collections::allocateIteratorsIterator is false, methods on libGDX collections return the same iterator instance each time the method is called. For nested or multithreaded iteration create a new iterator using the appropriate constructor. |
| GDXJavaUnusedTag | Unused class tag | Looks for unused class tags in GDXTag annotations. |
| GDXJsonDuplicateProperty | Duplicate property | Looks for properties which are defined multiple times in the same object. |
| GDXKotlinAssetsFileError | @GDXAssets problem | Checks for problems related to @GDXAssets annotations |
| GDXKotlinFlushInsideLoop | Flushing a batch inside a loop | Looks for the possibility of a flush of a batch or renderer occuring inside a loop, either directly or indirectly. <br />For performance reasons care should be taken to not cause unnecessary flushes, and to limit the number of flushes per frame as much as possible. <br />Note that calling SpriteBatch.draw(..) with a different texture also causes a flush, which is not detected by this inspection. Use a <a href="https://github.com/libgdx/libgdx/wiki/Texture-packer">Texture Atlas</a> instead of many different textures. |
| GDXKotlinInvalidPropertyKey | Property key does not exist | Looks for the use of property keys which do not exist. |
| GDXKotlinLogLevel | Overly verbose log level | Looks for calls to Gdx.app.setLogLevel() setting the log level to LOG_INFO or LOG_VERBOSE, which while useful for debugging should propably not be used in production builds. |
| GDXKotlinMissingFlush | Missing flush on Preferences | Call Preferences.flush() to make sure changes to preferences are persisted. |
| GDXKotlinNonExistingAsset | Resource doesn't exist | Looks in Java and Kotlin code for usages of resources which don't exist. @GDXAssets annotated elements only. |
| GDXKotlinProfilingCode | Profiling code | Looks for profiling code, which should be disabled before release. |
| GDXKotlinStaticResource | Static resource | Don't make resources static, unless you take care to properly manage them. Static resources can cause problems on Android, because the life-cycle of a static variable is not necessarily the same as the life-cycle of your application.<br /><br />Note that Kotlin top-level properties and properties of object literals and companion objects are compiled to static properties. |
| GDXKotlinTestId | Test IDs or dummy IDs | Looks for the use of some known test IDs and dummy IDs. |
| GDXKotlinUnsafeIterator | Use of non-reentrant iterator method | If Collections::allocateIteratorsIterator is false, methods on libGDX collections return the same iterator instance each time the method is called. For nested or multithreaded iteration create a new iterator using the appropriate constructor. |
| GDXKotlinUnusedTag | Unused class tag | Looks for unused class tags in GDXTag annotations. |
| GDXMalformedColorString | Malformed color string | Looks in Skin files for malformed color strings. |
| GDXMissingFilesPermission | Missing permissions for using external files | If you want to use external storage on Android you have to declare the appropriate permissions in AndroidManifest.xml. |
| GDXMissingProperty | Missing properties in Skin files | Looks for missing mandatory properties in Skin files. |
| GDXNonExistingClass | Non-existing classes in Skin files | Looks in Skin files for classes that don't exist and the use of non-static inner classes. |
| GDXNonExistingField | Non-existing fields in Skin files | Looks in Skin files for fields/properties that don't exist. |
| GDXNonExistingFile | Non-existing font files in Skin files | Looks for non-existing font files in Skin files. |
| GDXNonExistingResourceInAlias | Non-existing resources in Skin files | Looks in Skin files for resource aliases for resources which do not exist. |
| GDXOpenGLVersion | Missing OpenGL ES directive | If your application requires OpenGL ES 2 or 3 to function, add the appropriate declaration to the manifest: &lt;uses-feature android:glEsVersion="0x00020000" android:required="true" /&gt; or &lt;uses-feature android:glEsVersion="0x00030000" android:required="true" /&gt;. |
| GDXSkinDeprecated | Usage of deprecated class/field | Looks for the use of deprecated classes and fields in Skin files. |
| GDXSkinType | Type errors in Skin files | Looks for type errors in Skin files. |
| GDXToplevel | Invalid toplevel value | The toplevel value of a JSON file should be an object. |
| GDXXmlTestId | Test IDs or dummy IDs | Looks for the use of some known test IDs and dummy IDs. |
