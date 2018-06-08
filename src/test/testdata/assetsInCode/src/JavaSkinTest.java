import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

public class JavaSkinTest {

  @GDXAssets(skinFiles = {"src/assets/dir/holo.skin"}, atlasFiles = "")
  Skin skin;

  @GDXAssets(skinFiles = {"src/assets/dir/skin.json"}, atlasFiles = "")
  static Skin staticSkin;

  void m() {
    new com.badlogic.gdx.utils.Json().addClassTag("TagColor", com.badlogic.gdx.graphics.Color.class)
    new com.badlogic.gdx.utils.Json().addClassTag("TagStyle", com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle.class)
    new com.badlogic.gdx.utils.Json().addClassTag("TagTinted", com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable.class)
  }

}