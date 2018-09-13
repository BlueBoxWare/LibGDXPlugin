import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets
import com.gmail.blueboxware.libgdxplugin.annotations.GDXTag

@GDXTag("kotlinTag1", "kotlinTag2")
class KotlinClass

@GDXAssets(skinFiles = arrayOf("src/libgdx.skin"), atlasFiles = arrayOf("src/dir/holo.atlas"))
val skin1 = Skin()

@GDXAssets(atlasFiles = ["src/dir/holo.atlas"])
val atlas = TextureAtlas()

val atlas2 = TextureAtlas()

val c1 = skin1.getColor("yellow")
val c2 = skin1.getColor(<error descr="Resource \"blue\" with type \"com.badlogic.gdx.graphics.Color\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"blue"</error>)
val c3 = skin1.getColor(<error descr="Resource \"button\" with type \"com.badlogic.gdx.graphics.Color\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"button"</error>)
val blue = ""
val c4 = skin1.getColor("$blue")
val c5 = skin1.getColor("${"blue"}")

fun f() {
  @GDXAssets(skinFiles = arrayOf("src/libgdx.skin"))
  val skin2 = Skin()

  val skin3 = Skin()

  skin1.get(<error descr="Resource \"kt1\" with type \"KotlinClass\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"kt1"</error>, KotlinClass::class.java)
  skin1.get("kt2", KotlinClass::class.java)

  skin1.get(TextButton.TextButtonStyle::class.java)
  skin3.get(TextButton.TextButtonStyle::class.java)
  skin1.get(<error descr="Resource \"\" with type \"com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">""</error>, TextButton.TextButtonStyle::class.java)
  skin1.get("toggle", TextButton.TextButtonStyle::class.java)
  val userred = ""
  skin1.get("$userred", TextButton.TextButtonStyle::class.java)
  skin1.get("${userred}", TextButton.TextButtonStyle::class.java)
  skin1.get("${"userred"}", TextButton.TextButtonStyle::class.java)
  skin1.get(<error descr="Resource \"user-red\" with type \"com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"user-red"</error>, TextButton.TextButtonStyle::class.java)
  skin1.get("taggedStyle1", TextButton.TextButtonStyle::class.java)
  skin1.get(<error descr="Resource \"taggedStyle2\" with type \"com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"taggedStyle2"</error>, TextButton.TextButtonStyle::class.java)
  skin1.get("user-red", Skin.TintedDrawable::class.java)
  skin1.getDrawable("button-left")
  skin1.getDrawable("user-red")
  skin3.getDrawable("user-red")
  skin1.getRegion("button-left")
  skin1.getRegion(<error descr="Resource \"green\" with type \"com.badlogic.gdx.graphics.g2d.TextureRegion\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"green"</error>)
  skin1.get("taggedStyle2", Button.ButtonStyle::class.java)
  skin1.get(<error descr="Resource \"taggedStyle1\" with type \"com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle\" does not exist in files \"libgdx.skin\", \"holo.atlas\" or \"libgdx.atlas\"">"taggedStyle1"</error>, Button.ButtonStyle::class.java)
  @Suppress("LibGDXNonExistingAsset")
  skin1.get("taggedStyle1", Button.ButtonStyle::class.java)

  atlas.findRegion("button-left")
  atlas2.findRegion("button-left")
  atlas.findRegion(<error descr="Resource \"button-foo\" with type \"com.badlogic.gdx.graphics.g2d.TextureRegion\" does not exist in file \"holo.atlas\"">"button-foo"</error>)

  skin2.getRegion("button-back-disabled")
  skin2.getRegion(<error descr="Resource \"toolbar\" with type \"com.badlogic.gdx.graphics.g2d.TextureRegion\" does not exist in files \"libgdx.skin\" or \"libgdx.atlas\"">"toolbar"</error>)
}