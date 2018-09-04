import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Colors

fun defs() {
  Colors.put("singlePutKotlin", Color.PURPLE)
  Colors.getColors().put("singleGetColorsKotlin", Color.CYAN)
  Colors.put("multiKotlin", Color.YELLOW)
  Colors.put("multiKotlin", Color.RED)

  Colors.get("c")
  Colors.getColors().get("c")
}