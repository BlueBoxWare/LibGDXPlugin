import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

class Test1 {

  @GDXAssets(skinFiles = {"src/test.skin"})
  Skin skin;
  Drawable drawable = skin.getDrawable("te<caret>st");

}
