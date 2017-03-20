import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class FindUsages3 {

  @GDXAssets(atlasFiles = "src\\findUsages/findUsages3.atlas")
  static Skin staticSkin;

  void f() {
    staticSkin.get("button-back-disabled", TextureRegion.class);
    staticSkin.getDrawable("button-back-disabled");
    staticSkin.has("button-back-disabled",  TextureRegion.class);
    staticSkin.getSprite("button-back-disabled");
  }

}