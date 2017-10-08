@MyAnnotation(argsNoDefault = arrayOf("foo", "oof"), arg = "bar")
var String: String? = "foo"

@MyAnnotation(argNoDefault = arrayOf("object"))
object KotlinObject {
  fun m() {

  }

  @MyAnnotation(args = arrayOf("123", "456"), argsNoDefault = arrayOf("789"))
  fun annotatedMethod {

  }

  val s = ""
  @MyAnnotation(arg = "xyz")
  val t = ""

  fun f(): KotlinClass {
    return KotlinClass()
  }

}

@MyAnnotation(argNoDefault = "kotlin", args = arrayOf("123"))
class KotlinClass {

  @MyAnnotation(args = arrayOf("a", "b"))
  val string: String? = "string"

  @MyAnnotation(args = arrayOf("a", "b"))
  val kotlinClass = KotlinClass()

  val kotlinClassNA: KotlinClass? = KotlinClass()

  fun m() {}

  @MyAnnotation(args = arrayOf("me", "thod"))
  fun annotatedMethod() {}


  companion object {

    @MyAnnotation(argsNoDefault = arrayOf("xyz"))
    val s = ""

    fun coMethod() {}

    fun f(): KotlinClass? {

      @MyAnnotation(arg = "local")
      val l = 3

      return KotlinClass()
    }

    @MyAnnotation(argsNoDefault = arrayOf("g"))
    fun g() {}
  }

}

class SubClass: KotlinClass {

}