import com.badlogic.gdx.graphics.Texture

<warning>val topLevelTexture: Texture = Texture("")</warning>

@Suppress("UNUSED_VARIABLE", "UNCHECKED_CAST", "USELESS_CAST")
public class Test {

  companion object {
    <warning>val companionTexture: Texture = Texture("")</warning>
  }

  val fieldTexture: Texture = Texture("")

  fun test() {
    val localTexture: Texture = Texture("")
  }

}
