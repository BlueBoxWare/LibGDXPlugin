import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

@GDXAssets(atlasFiles = arrayOf(""), skinFiles = arrayOf("src/libgdx.skin"))
val skin: Skin = Skin()

val s = "ff00ff"
val c1 = <weak_warning descr="#ff0000ff">"#ff0000"</weak_warning>
val c2 = <weak_warning descr="#00006443">Color(25667)</weak_warning>
val c3 = <weak_warning descr="#00ff00ff">Color(0f, 1f, 0f, 1f)</weak_warning>
val c4 = <weak_warning descr="#00ffff99">Color.valueOf("00ffff99")</weak_warning>
val c5 = Color.PINK
val c6 = Color.valueOf(<weak_warning descr="#ff0000ff">c1</weak_warning>)
val c7 = <weak_warning descr="#ff00ffff">Color.valueOf(s)</weak_warning>

val resource = "red"
val d1 = <weak_warning descr="#ff0000ff">skin.getColor("red")</weak_warning>
val d2 = <weak_warning descr="#ff0000ff">skin.getColor(resource)</weak_warning>
val d3 = <weak_warning descr="#ff0000ff">skin.get(resource, Color::class.java)</weak_warning>
val d4 = <weak_warning descr="#ffffffff">skin.optional("white", Color::class.java)</weak_warning>
val da = skin.optional("white", Drawable::class.java)
val db = skin.optional("white", 3)
val d5 = skin.get(Color::class.java)
val d6 = <weak_warning descr="#ff0000ff">d1</weak_warning>
val d7 = <weak_warning descr="#ff0000ff">d2</weak_warning>
val d8 = <weak_warning descr="#ff0000ff">d3</weak_warning>
val d9 = <weak_warning descr="#ffffffff">d4</weak_warning>


object O {
  val s = "ff00ff"
  val c1 = <weak_warning descr="#ff0000ff">"#ff0000"</weak_warning>
  val c2 = <weak_warning descr="#00006443">Color(25667)</weak_warning>
  val c3 = <weak_warning descr="#00ff00ff">Color(0f, 1f, 0f, 1f)</weak_warning>
  val c4 = <weak_warning descr="#00ffff99">Color.valueOf("00ffff99")</weak_warning>
  val c5 = Color.PINK
  val c6 = Color.valueOf(<weak_warning descr="#ff0000ff">c1</weak_warning>)
  val c7 = <weak_warning descr="#ff00ffff">Color.valueOf(s)</weak_warning>

  val cc1 = <weak_warning descr="#ff0000ff">d6</weak_warning>
  val cc2 = <weak_warning descr="#ff0000ff">d7</weak_warning>
  val cc3 = <weak_warning descr="#ff0000ff">d8</weak_warning>
  val cc4 = <weak_warning descr="#ffffffff">d9</weak_warning>
}

class C {

  val ss = s
  val c1 = <weak_warning descr="#ff0000ff">"#ff0000"</weak_warning>
  val c2 = <weak_warning descr="#00006443">Color(25667)</weak_warning>
  val c3 = <weak_warning descr="#00ff00ff">Color(0f, 1f, 0f, 1f)</weak_warning>
  val c4 = <weak_warning descr="#00ffff99">Color.valueOf("00ffff99")</weak_warning>
  val c5 = Color.PINK
  val c6 = Color.valueOf(<weak_warning descr="#ff0000ff">c1</weak_warning>)
  val c7 = <weak_warning descr="#ff00ffff">Color.valueOf(s)</weak_warning>

  companion object {
    val ss = s
    val c1 = <weak_warning descr="#ff0000ff">"#ff0000"</weak_warning>
    val c2 = <weak_warning descr="#00006443">Color(25667)</weak_warning>
    val c3 = <weak_warning descr="#00ff00ff">Color(0f, 1f, 0f, 1f)</weak_warning>
    val c4 = <weak_warning descr="#00ffff99">Color.valueOf("00ffff99")</weak_warning>
    val c5 = Color.PINK
    val c6 = Color.valueOf(<weak_warning descr="#ff0000ff">c1</weak_warning>)
    val c7 = <weak_warning descr="#ff00ffff">Color.valueOf(s)</weak_warning>

    object O {
      val s = "ff00ff"
      val c1 = <weak_warning descr="#ff0000ff">"#ff0000"</weak_warning>
      val c2 = <weak_warning descr="#00006443">Color(25667)</weak_warning>
      val c3 = <weak_warning descr="#00ff00ff">Color(0f, 1f, 0f, 1f)</weak_warning>
      val c4 = <weak_warning descr="#00ffff99">Color.valueOf("00ffff99")</weak_warning>
      val c5 = Color.PINK
      val c6 = Color.valueOf(<weak_warning descr="#ff0000ff">c1</weak_warning>)
      val c7 = <weak_warning descr="#ff00ffff">Color.valueOf(s)</weak_warning>
    }
  }
}

fun f() {
  val x1 = <weak_warning descr="#ff0000ff">c1</weak_warning>
  val x2 = <weak_warning descr="#00006443">c2</weak_warning>
  val x3 = <weak_warning descr="#00ff00ff">c3</weak_warning>
  val x4 = <weak_warning descr="#00ffff99">c4</weak_warning>
  val x5 = c5
  val x6 = <weak_warning descr="#ff0000ff">c6</weak_warning>
  val x7 = <weak_warning descr="#ff00ffff">c7</weak_warning>

  val z1 = <weak_warning descr="#ff0000ff">x1</weak_warning>
  val z2 = <weak_warning descr="#00006443">x2</weak_warning>
  val z3 = <weak_warning descr="#00ff00ff">x3</weak_warning>
  val z4 = <weak_warning descr="#00ffff99">x4</weak_warning>
  val z5 = x5
  val z6 = <weak_warning descr="#ff0000ff">x6</weak_warning>
  val z7 = <weak_warning descr="#ff00ffff">x7</weak_warning>

  val zz = <weak_warning descr="#ff00ffff">z7</weak_warning>

  val f = {
    val c = <weak_warning descr="#ff0000ff">z1</weak_warning>
    val d = <weak_warning descr="#ff0000ff">c</weak_warning>
  }

}

val os = O.s
val o1 = <weak_warning descr="#ff0000ff">O.c1</weak_warning>
val o2 = <weak_warning descr="#00006443">O.c2</weak_warning>
val o3 = <weak_warning descr="#00ff00ff">O.c3</weak_warning>
val o4 = <weak_warning descr="#00ffff99">O.c4</weak_warning>
val o5 = O.c5
val o6 = <weak_warning descr="#ff0000ff">O.c6</weak_warning>
val o7 = <weak_warning descr="#ff00ffff">O.c7</weak_warning>

val Cs = C.ss
val cc1 = <weak_warning descr="#ff0000ff">C.c1</weak_warning>
val cc2 = <weak_warning descr="#00006443">C.c2</weak_warning>
val cc3 = <weak_warning descr="#00ff00ff">C.c3</weak_warning>
val cc4 = <weak_warning descr="#00ffff99">C.c4</weak_warning>
val cc5 = C.c5
val cc6 = <weak_warning descr="#ff0000ff">C.c6</weak_warning>
val cc7 = <weak_warning descr="#ff00ffff">C.c7</weak_warning>

val cco1 = <weak_warning descr="#ff0000ff">C.Companion.O.c1</weak_warning>
val cco2 = <weak_warning descr="#00006443">C.Companion.O.c2</weak_warning>
val cco3 = <weak_warning descr="#00ff00ff">C.Companion.O.c3</weak_warning>
val cco4 = <weak_warning descr="#00ffff99">C.Companion.O.c4</weak_warning>
val cco5 = C.Companion.O.c5
val cco6 = <weak_warning descr="#ff0000ff">C.Companion.O.c6</weak_warning>
val cco7 = <weak_warning descr="#ff00ffff">C.Companion.O.c7</weak_warning>

val doofwe = <weak_warning descr="#ff00ffff">Color.valueOf(s)</weak_warning>

val co1 = <weak_warning descr="#ff0000ff">C().c1</weak_warning>
val co2 = <weak_warning descr="#00006443">C().c2</weak_warning>
val co3 = <weak_warning descr="#00ff00ff">C().c3</weak_warning>
val co4 = <weak_warning descr="#00ffff99">C().c4</weak_warning>
val co5 = C().c5
val co6 = <weak_warning descr="#ff0000ff">C().c6</weak_warning>
val co7 = <weak_warning descr="#ff00ffff">C().c7</weak_warning>


fun f(a: Any) {

}

fun ff() {
  f("334455")
  f(<weak_warning descr="#334455ff">"#334455"</weak_warning>)
  f(<weak_warning descr="#00000158">Color(344)</weak_warning>)
  f(<weak_warning descr="#00ff80ff">Color(0f, 1f, .5f, 1f)</weak_warning>)
  f(<weak_warning descr="#332211ff">Color.valueOf("332211")</weak_warning>)
  f(<weak_warning descr="#ff0000ff">co1</weak_warning>)
  f(<weak_warning descr="#00006443">co2</weak_warning>)
  f(<weak_warning descr="#00ff00ff">co3</weak_warning>)
  f(<weak_warning descr="#00ffff99">co4</weak_warning>)
  f(co5)
  f(<weak_warning descr="#ff0000ff">co6</weak_warning>)
  f(<weak_warning descr="#ff00ffff">co7</weak_warning>)

  val f1 = 0f
  val f2 = 1f

  val dc = <weak_warning descr="#ff00ffff">Color(f2, f1, f2, 1f)</weak_warning>
}

val m = mapOf<Any, Any>(
        1 to <weak_warning descr="#ff0000ff">co1</weak_warning>,
        <weak_warning descr="#00ffff99">cc4</weak_warning> to 2,
Color.VIOLET to 3
)

val i = 3456
val ci = <weak_warning descr="#00000d80">Color(i)</weak_warning>