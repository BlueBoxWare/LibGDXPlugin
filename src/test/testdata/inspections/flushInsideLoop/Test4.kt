import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Matrix4

fun t() {

  val o: Any = Any()

  while (MathUtils.randomBoolean()) {


    if (o is SpriteBatch) {
      o.<warning>setTransformMatrix(Matrix4())</warning>
    } else if (o is CpuSpriteBatch) {
      o.setTransformMatrix(Matrix4())
    }

    fun f() {
      fun g() {
        SpriteBatch().flush()
      }

      g()
    }

    <warning>f()</warning>

    val fff =  { x: Any ->
      fun g() {
        if (x is Int) {
          SpriteBatch().flush()
        }
      }
      g()
    }

    <warning>fff(1)</warning>


  }

  val ff = fun() {
    fun g() {
      SpriteBatch().flush()
    }
    g()
  }

  val gg = ff
  val hh = gg

  while (MathUtils.randomBoolean()) {
    <warning>gg()</warning>
    <warning>hh()</warning>
  }

}