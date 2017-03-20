import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets
import com.badlogic.gdx.scenes.scene2d.utils.Drawable

@GDXAssets(atlasFiles = arrayOf("src\\findUsages/findUsages3.atlas"))
val s: Skin = Skin()

fun f() {
  val c = s.get("button-back-disabled", Drawable::class.java)
  val d = s.getPatch("button-back-disabled")
}