@file:Suppress("UNUSED_PARAMETER")

fun compile(a : Any) {}
fun compile(group: String, name: String, version: String? = null) {}

fun dependencies(a: Any) {}

val gdxVersion = "1.9.0"
val gdx = "1.9.0"
val visUiVersion = ""
val ktxCollectionsVersion = ""
val websocketSerializationVersion = ""
val websocketVersion = ""
val overlap2dVersion = ""
val ktxVisStyleVersion = ""
val noise4jVersion = ""
val ktxScene2DVersion = ""
val bladeInkVersion = ""
val jaciVersion = ""
val utilsBox2dVersion = ""

fun f() {
    dependencies {
      compile(<warning descr="A newer version of LibGDX is available (version 1.9.5)">"com.badlogicgames.gdx:gdx:1.9.2"</warning>)
      compile<warning descr="A newer version of LibGDX is available (version 1.9.5)">(group = "com.badlogicgames.gdx", name = "gdx", version = "1.9.4")</warning>
      compile("com.badlogicgames.gdx:gdx:1.9.8")
      compile(group = "com.badlogicgames.gdx", name = "gdx", version = "1.9.8")

      compile("com.underwaterapps.overlap2druntime:overlap2d-runtime-libgdx:0.1.1")
      compile(<warning descr="A newer version of Overlap2D is available (version 0.1.1)">"com.underwaterapps.overlap2druntime:overlap2d-runtime-libgdx:0.1.0"</warning>)

      compile(group = "com.underwaterapps.overlap2druntime", name = "overlap2d-runtime-libgdx", version = "0.1.1")
      compile<warning descr="A newer version of Overlap2D is available (version 0.1.1)">(group = "com.underwaterapps.overlap2druntime", name = "overlap2d-runtime-libgdx", version = "0.1.0")</warning>
      compile<warning descr="A newer version of Overlap2D is available (version 0.1.1)">(group = "com.underwaterapps.overlap2druntime", name = "overlap2d-runtime-libgdx")</warning>

      compile(<warning>"com.badlogicgames.gdx:gdx:$gdxVersion"</warning>)
      compile(<warning>"com.badlogicgames.gdx:gdx:$gdx"</warning>)

      compile(<warning descr="A newer version of VisUI is available (version 1.2.5)">"com.kotcrab.vis:vis-ui:$visUiVersion"</warning>)
      compile("com.github.czyzby:ktx-collections:$ktxCollectionsVersion")
      compile("com.github.czyzby:gdx-websocket-serialization:$websocketSerializationVersion")
      compile("com.github.czyzby:gdx-websocket:$websocketVersion")
      compile(<warning descr="A newer version of Overlap2D is available (version 0.1.1)">"com.underwaterapps.overlap2druntime:overlap2d-runtime-libgdx:$overlap2dVersion"</warning>)
      compile("com.github.czyzby:ktx-vis-style:$ktxVisStyleVersion")
      compile("com.github.czyzby:noise4j:$noise4jVersion")
      compile("com.github.czyzby:ktx-scene2d:$ktxScene2DVersion")
      compile("com.bladecoder.ink:blade-ink:$bladeInkVersion")
      compile("com.github.ykrasik:jaci-libgdx-cli-java:$jaciVersion")
    }
}
