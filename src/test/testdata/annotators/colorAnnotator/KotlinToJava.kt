import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Colors

const val constC = "#ffff00"

val s = "ff00ff"
val c1 = "#ff0000"

class C {

  val c2 = Color(25667)
  val c3 = Colors.get("RED")

  fun f() {
    Colors.put("foobar", Color.YELLOW)
  }

}
