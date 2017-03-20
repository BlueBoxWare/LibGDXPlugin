import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

class JavaSkinTest {

  @GDXAssets(skinFiles = {"src/assets/dir/holo.skin"}, atlasFiles = "")
  Skin skin;

  @GDXAssets(skinFiles = {"src/assets/dir/skin.json"}, atlasFiles = "")
  static Skin staticSkin;

}