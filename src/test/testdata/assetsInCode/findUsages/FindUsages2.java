import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

class FindUsages1 {

  @GDXAssets(skinFiles = "src\\findUsages/findUsages2.skin")
  static Skin staticSkin;

  @GDXAssets(skinFiles = {"src/findUsages/findUsages1.skin", "src\\findUsages\\findUsages2.skin"})
  private Skin skin;

  void f() {
    staticSkin.get("toggle", TextButton.TextButtonStyle.class);
    skin.remove("toggle", TextButton.TextButtonStyle.class)
  }

}