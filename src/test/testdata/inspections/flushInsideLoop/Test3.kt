package flushInsideLoop

import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Matrix4

@Suppress("UNUSED_VARIABLE")
fun t() {

  while (MathUtils.randomBoolean()) {

    fun f() {
      SpriteBatch().flush()
    }

    CpuSpriteBatch().setTransformMatrix(Matrix4())

    val c = Test3()
    val d = <warning>Test3(SpriteBatch())</warning>
    val e = <warning>Test3(1)</warning>
    val f = Test3(1.0f)

    c.<warning>method1()</warning>
    c.<warning>method2(SpriteBatch())</warning>
    c.method3()

    Test3.<warning>sMethod()</warning>


  }

}