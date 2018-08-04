import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Json;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXTag;

@GDXTag({"javaTag1", "javaTag2"})
public class Test {

  @GDXAssets(skinFiles = { "src/libgdx.skin"}, atlasFiles = { "src/dir/holo.atlas" })
  Skin skin = new Skin();

  @GDXAssets(skinFiles = "src/libgdx.skin")
  Skin skin2;

  @GDXAssets(atlasFiles = "src/dir/holo.atlas")
  TextureAtlas atlas;

  TextureAtlas atlas2;

  Skin skin3;

  void m() {
    new Json().addClassTag("TagStyle", Button.ButtonStyle.class);

    skin.get("jt1", Test.class);
    skin.get(<error descr="Resource \"jt2\" with type \"Test\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"jt2"</error>, Test.class);

    skin.getColor("yellow");
    skin3.getColor("yellow");
    skin.getColor(<error descr="Resource \"blue\" with type \"com.badlogic.gdx.graphics.Color\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"blue"</error>);
    skin.getColor(<error descr="Resource \"button\" with type \"com.badlogic.gdx.graphics.Color\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"button"</error>);
    skin.get(TextButton.TextButtonStyle.class);
    skin.get(<error descr="Resource \"\" with type \"com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">""</error>, TextButton.TextButtonStyle.class);
    skin.get("toggle", TextButton.TextButtonStyle.class);
    skin.get(<error descr="Resource \"user-red\" with type \"com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"user-red"</error>, TextButton.TextButtonStyle.class);
    skin.get("taggedStyle1", TextButton.TextButtonStyle.class);
    skin.get(<error descr="Resource \"taggedStyle2\" with type \"com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"taggedStyle2"</error>, TextButton.TextButtonStyle.class);
    //noinspection LibGDXNonExistingAsset
    skin.get("taggedStyle2", TextButton.TextButtonStyle.class);
    skin3.get("taggedStyle2", TextButton.TextButtonStyle.class);
    skin.get("user-red", Skin.TintedDrawable.class);
    skin.getDrawable("button-left");
    skin.getDrawable("user-red");
    skin.getRegion("button-left");
    skin.getRegion(<error descr="Resource \"green\" with type \"com.badlogic.gdx.graphics.g2d.TextureRegion\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"green"</error>);
    skin.get("taggedStyle2", Button.ButtonStyle.class);
    skin.get(<error descr="Resource \"taggedStyle1\" with type \"com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"taggedStyle1"</error>, Button.ButtonStyle.class);

    atlas.findRegion("button-left");
    atlas2.findRegion("foo");
    atlas.findRegion(<error descr="Resource \"button-foo\" with type \"com.badlogic.gdx.graphics.g2d.TextureRegion\" does not exist in file \"holo.atlas\"">"button-foo"</error>);

    skin2.getRegion("button-back-disabled");
    skin2.getRegion(<error descr="Resource \"toolbar\" with type \"com.badlogic.gdx.graphics.g2d.TextureRegion\" does not exist in files \"libgdx.skin\" or \"libgdx.atlas\"">"toolbar"</error>);
  }

}