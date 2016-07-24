import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils

open class A: SpriteBatch() {

  override fun flush() {
    super.flush()
  }
}

open class B: A() {
  override fun flush() {
    super.flush()
  }
}

class C: B() {
  override fun flush() {

  }

  fun myFlush() {
    super.flush()
  }
}

fun main() {
  while(MathUtils.randomBoolean()) {
    A().<warning>flush()</warning>
    B().<warning>flush()</warning>
    C().flush()
    C().<warning>myFlush()</warning>

    B().let {
      it.<warning>flush()</warning>
    }

    B().apply { <warning>flush()</warning> }

    val a = A()
    val b = a
    val c = b

    c.let {
      it.apply {
        <warning>flush()</warning>
      }
    }
  }
}