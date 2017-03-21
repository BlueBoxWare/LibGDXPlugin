import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

class FindUsages3 {

  @GDXAssets(skinFiles = "src\\findUsages/findUsages4.skin")
  static Skin staticSkin;

  void f() {
    staticSkin.getRegion("wallpaper");
    staticSkin.getDrawable("wallpaper");
    staticSkin.optional("wallpaper",  TextureRegion.class);
    staticSkin.getPatch("wallpaper");
  }

}