import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

class Test {

  @GDXAssets(skinFiles = arrayOf("src\\libgdx.skin", <error>"src/libgd.skin"</error>), atlasFiles = arrayOf("src//dir/holo.atlas"))
  val skin1 = Skin()

  @GDXAssets(skinFiles = arrayOf("src/../src//libgdx.skin", <error>"src\\libgd.skin"</error>), atlasFiles = arrayOf(<error>"src/dir/hol.atlas"</error>))
  val atlas1 = TextureAtlas()

  companion object {

    object O {
      @Suppress("UNUSED_VARIABLE")
      fun f() {
        @GDXAssets(skinFiles = arrayOf(<error>"android/tubular/skin/tubular-ui.json"</error>, "src//libgdx.skin"), atlasFiles = arrayOf("src/dir/holo.atlas", <error>"src/dir/holo"</error>))
        val skin: Skin = Skin()
      }

    }
  }

}