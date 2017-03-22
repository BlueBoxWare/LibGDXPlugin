import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

class Test {

  @GDXAssets(skinFiles = "src/dir\\holo.skin", atlasFiles = <error>"src/dir\\hol.skin"</error>)
  Skin skin1;

  @GDXAssets(skinFiles = {<error>"src\\libgdx"</error>, "src\\libgdx.skin", "src\\libgdx.atlas"}, atlasFiles = {"src/dir/holo.atlas", <error>"src/dir/holo"</error>})
  Skin skin2;

  @GDXAssets(skinFiles = <error>"src/dir\\holo.kin"</error>, atlasFiles = {"src/dir\\holo.skin"})
  TextureAtlas atlas1;

  @GDXAssets(skinFiles = {}, atlasFiles = {<error>""</error>})
  TextureAtlas atlas2;

}