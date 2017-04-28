import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

class Test2 {

  @GDXAssets(atlasFiles = {"src/test.atlas"})
  TextureAtlas ta;
  TextureRegion tr = ta.findRegion("us<caret>er");

}
