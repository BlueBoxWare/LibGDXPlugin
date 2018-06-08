import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

@GDXAssets(atlasFiles = arrayOf(""), skinFiles = arrayOf("src\\findUsages/findUsagesWithTags1.skin"))
val s: Skin = Skin()

fun f() {
  val c = s.get("red", Color::class.java)
}