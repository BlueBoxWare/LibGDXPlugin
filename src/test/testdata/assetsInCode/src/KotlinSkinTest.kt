import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

@GDXAssets(atlasFiles = arrayOf(""), skinFiles = arrayOf("src/assets/dir/holo.skin"))
val skin: Skin = Skin()

class KotlinSkinTest {

  @GDXAssets(atlasFiles = arrayOf(""), skinFiles = arrayOf("src/assets/libgdx.skin"))
  val skin = Skin()

  companion object {
    @GDXAssets(atlasFiles = arrayOf(""), skinFiles = arrayOf("src/assets/dir/skin.json"))
    val skin = Skin()
  }

}