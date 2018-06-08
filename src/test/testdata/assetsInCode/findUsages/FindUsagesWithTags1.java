import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

class FindUsages1 {

  @GDXAssets(skinFiles = "src\\findUsages/findUsagesWithTags1.skin")
  static Skin staticSkin;

  void f() {
    staticSkin.get("red", Color.class);
    new com.badlogic.gdx.utils.Json().addClassTag("Tag", com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle.class)
  }

}