import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

class FindUsages1 {

  @GDXAssets(skinFiles = "src\\findUsages/findUsages1.skin")
  static Skin staticSkin;

  void f() {
    staticSkin.get("red", Color.class);
  }

}