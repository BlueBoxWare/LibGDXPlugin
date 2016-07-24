import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Predicate

@Suppress("UNUSED_VARIABLE")
class ArrayTest {

  inner class Pred : Predicate<String> {
    override fun evaluate(arg0: String): Boolean {
      return false
    }
  }

  fun test() {
    val strings = Array<String>()

    val iterator1: Iterator<Any> = <warning>strings.iterator()</warning>
    val iterator2: Iterable<Any> = <warning>strings.select(Pred())</warning>

    for (s in <warning>strings</warning>) println(s)
    for (s in <warning>Array<String>()</warning>) println(s)

    println(iterator1.toString())

  }

}