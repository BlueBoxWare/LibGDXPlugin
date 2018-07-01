package LibGDXPlugin.testdata.inspections.flushInsideLoop

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer


class Class1(s: SpriteBatch) {
  init {
    s.flush()
  }
}

class Class2 {
  init {
    SpriteBatch().flush()
  }
}

class Class3 {

  constructor() {}

  @Suppress("UNUSED_PARAMETER")
  constructor(i: Int) {
    SpriteBatch().end()
  }

}

class Class4() {

  init {
    SpriteBatch().end()
  }

  @Suppress("UNUSED_PARAMETER")
  constructor(i: Int): this() {

  }

}

class Class5 {
  constructor(o: Any) {
    if (o is SpriteBatch) {
      o.flush()
    }
  }
}

class Class6(val s: SpriteBatch) {
  var i: Int = 1
    get() {
      s.flush()
      return 3
    }
}

class Class7() {
  var i: Int
    get() = 1
    @Suppress("UNUSED_PARAMETER")
    set(value) {
      SpriteBatch().transformMatrix = null
    }
}

class Class8() {
  fun method() {
    SpriteBatch().flush()
  }
}

@Suppress("UNREACHABLE_CODE")
fun test() {
  do {
    <warning>Class1(SpriteBatch())</warning>
    <warning>Class2()</warning>
    Class3()
    <warning>Class3(3)</warning>
    <warning>Class4()</warning>
    <warning>Class4(4)</warning>
    <warning>Class5(3)</warning>
    <warning>Class6(SpriteBatch()).i</warning>
    Class6(SpriteBatch()).i = 3
    Class7().i
    <warning>Class7().i</warning> = 1
    Class8().<warning>method()</warning>
  } while (true)

  if (false) {
    SpriteBatch().let {
      while (true) {
        it.<warning>flush()</warning>
        fun g() {
          it.projectionMatrix = null
        }
        <warning>g()</warning>
        val e =  { SpriteBatch().flush() }
        <warning>e()</warning>

      }
    }
  }
}

fun test1() {
  while(true) {
    while(true) {
      CpuSpriteBatch().transformMatrix = null
    }
  }
}

fun test2() {

  val f = { SpriteBatch().flush() }

  while(false) {
    <warning>f()</warning>
  }

  while(true) {
    SpriteBatch().color = null
    <warning>SpriteBatch().shader</warning> = null
  }


}


fun ff(): () -> Unit  = { SpriteBatch().end() }


fun test3() {

  for (i in 0..10) {
    <warning>ff()()</warning>
    val q = ff()
    q()
  }

}

fun a1(o: Any?) = if (o is SpriteBatch) o.flush() else null

fun a2(u: Any? = null) = a1(u)

fun a3() = a2()

fun test4() {
  while(false) { <warning>a3()</warning> }

  val f = { o: Any -> (o as? ShapeRenderer)?.flush() }

  while(false) <warning>f(Any())</warning>
}

fun test5() {

  val f1:   () -> Unit = {   SpriteBatch().flush() }

  while(false) {
    <warning>f1()</warning>
  }

  while (false) {
    ImmediateModeRenderer20(true, true, 4).<warning>flush()</warning>
    <warning>SpriteBatch().transformMatrix</warning> = null
    CpuSpriteBatch().transformMatrix = null
    val b: Batch = SpriteBatch()
    b.<warning>flush()</warning>
  }
}

