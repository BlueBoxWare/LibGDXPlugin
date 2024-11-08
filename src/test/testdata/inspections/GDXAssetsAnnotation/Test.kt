import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

class TestFoo {

  @GDXAssets(skinFiles = arrayOf("src\\libgdx.skin", <error>"src/libgd.skin"</error>), atlasFiles = arrayOf("src//dir/holo.atlas"),
    <warning>propertiesFiles</warning> = arrayOf("src/test.properties", <error>"src/foo.properties"</error>)
  )
  val skin1 = Skin()

  @GDXAssets(<warning>skinFiles</warning> = ["src/../src//libgdx.skin", <error>"src\\libgd.skin"</error>], atlasFiles = arrayOf(<error>"src/dir/hol.atlas"</error>))
  val atlas1 = TextureAtlas()

  companion object {

    object O {
      @Suppress("UNUSED_VARIABLE")
      fun f() {
        @GDXAssets(skinFiles = arrayOf(<error>"android/tubular/skin/tubular-ui.json"</error>, "src//libgdx.skin"), atlasFiles = ["src/dir/holo.atlas", <error>"src/dir/holo"</error>])
        val skin: Skin = Skin()

        <warning>@GDXAssets(<warning>skinFiles</warning> = [])</warning>
        val a: Int? = null

        @GDXAssets(skinFiles = [])
        val b: B? = null
      }

    }
  }

}

open class A: Skin()

class B: A()

<warning>@GDXAssets(<warning>skinFiles</warning> = [])</warning>
val s1 = ""

@GDXAssets(skinFiles = [], <warning>propertiesFiles</warning> = [])
val s2 = B()

<warning>@GDXAssets(<warning>skinFiles</warning> = [<error>"bar"</error>])</warning>
val s3: String? = ""

@GDXAssets(skinFiles = [<error>"foo"</error>], <warning>propertiesFiles</warning> = [])
val s4: A? = null
