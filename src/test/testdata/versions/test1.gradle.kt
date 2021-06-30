@file:Suppress("UNUSED_PARAMETER")

fun compile(a : Any) {}
fun compile(group: String, name: String, version: String? = null) {}

fun dependencies(a: Any) {}

val gdxVersion = "1.9.0"
val gdx = "1.9.0"

fun f() {
    dependencies {
        compile(<warning descr="A newer version of LibGDX is available (version 1.9.5)">"com.badlogicgames.gdx:gdx:1.9.2"</warning>)
        compile<warning descr="A newer version of LibGDX is available (version 1.9.5)">(group = "com.badlogicgames.gdx", name = "gdx", version = "1.9.4")</warning>
        compile("com.badlogicgames.gdx:gdx:1.9.8")
        compile(group = "com.badlogicgames.gdx", name = "gdx", version = "1.9.8")

        compile("com.underwaterapps.overlap2druntime:overlap2d-runtime-libgdx:0.1.1")
        compile(<warning descr="A newer version of Overlap2D is available (version 0.1.1)">"com.underwaterapps.overlap2druntime:overlap2d-runtime-libgdx:0.1.0"</warning>)
        @Suppress("GDXOutdatedVersionGradleKotlin")
        compile("com.underwaterapps.overlap2druntime:overlap2d-runtime-libgdx:0.1.0")

        compile(group = "com.underwaterapps.overlap2druntime", name = "overlap2d-runtime-libgdx", version = "0.1.1")
        compile<warning descr="A newer version of Overlap2D is available (version 0.1.1)">(group = "com.underwaterapps.overlap2druntime", name = "overlap2d-runtime-libgdx", version = "0.1.0")</warning>

        compile(<warning descr="A newer version of LibGDX is available (version 1.9.5)">"com.badlogicgames.gdx:gdx:$gdxVersion"</warning>)
        compile(<warning descr="A newer version of LibGDX is available (version 1.9.5)">"com.badlogicgames.gdx:gdx:$gdx"</warning>)
    }
}
