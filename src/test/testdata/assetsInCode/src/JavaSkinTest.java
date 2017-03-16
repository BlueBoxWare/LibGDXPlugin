import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXSkin;

class JavaSkinTest {

  @GDXSkin(skinFiles = {"src/assets/dir/holo.skin"}, atlasFiles = "")
  Skin skin;

  @GDXSkin(skinFiles = {"src/assets/dir/skin.json"}, atlasFiles = "")
  static Skin staticSkin;

}