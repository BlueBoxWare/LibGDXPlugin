import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

class FindUsages1 {

  @GDXAssets(skinFiles = "src\\findUsages/findUsagesWithTags2.skin")
  static Skin staticSkin;

  @GDXAssets(skinFiles = {"src/findUsages/findUsagesWithTags1.skin", "src\\findUsages\\findUsagesWithTags2.skin"})
  private Skin skin;

  void f() {
    staticSkin.get("toggle", TextButton.TextButtonStyle.class);
    skin.remove("toggle", TextButton.TextButtonStyle.class)
  }

}