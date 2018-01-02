import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

class Test {

  @GDXAssets(skinFiles = "src/dir\\holo.skin", atlasFiles = <error>"src/dir\\hol.skin"</error>,
          <warning>propertiesFiles</warning> = {"src\\test.properties", <error>"src/foo.properties"</error>}
  )
  Skin skin1;

  @GDXAssets(skinFiles = {<error>"src\\libgdx"</error>, "src\\libgdx.skin", "src\\libgdx.atlas"}, atlasFiles = {"src/dir/holo.atlas", <error>"src/dir/holo"</error>})
  Skin skin2;

  @GDXAssets(<warning>skinFiles</warning> = <error>"src/dir\\holo.kin"</error>, atlasFiles = {"src/dir\\holo.skin"})
  TextureAtlas atlas1;

  @GDXAssets(<warning>skinFiles</warning> = {}, atlasFiles = {""})
  TextureAtlas atlas2;

  @GDXAssets(<warning>skinFiles</warning> = {})
  TA ta;

  @GDXAssets(<warning>propertiesFiles</warning> = {}, atlasFiles = <error>"src/dir\\hol.skin"</error>)
  TAA taa;

  <warning>@GDXAssets(<warning>skinFiles</warning> = {})</warning>
  int s;

  <warning>@GDXAssets(<warning>skinFiles</warning> = {})</warning>
  String str;
}

class TA extends TextureAtlas {}

class TAA extends TA {}