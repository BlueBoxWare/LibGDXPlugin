@MyAnnotation(argsNoDefault = ["foo", "oof"], arg = "bar")
var string: String? = "foo"

@MyAnnotation(argNoDefault = "object")
object KotlinObject {
  fun m() {

  }

  @MyAnnotation(args = arrayOf("123", "456"), argsNoDefault = ["789"])
  fun annotatedMethod() {

  }

  val s = ""
  @MyAnnotation(arg = "xyz")
  val t = ""

  fun f(): KotlinClass {
    return KotlinClass()
  }

}

@MyAnnotation(argNoDefault = "kotlin", args = ["123"])
open class KotlinClass {

  @MyAnnotation(args = ["a", "b"])
  val string: String? = "string"

  @MyAnnotation(args = arrayOf("a", "b"))
  val kotlinClass = KotlinClass()

  val kotlinClassNA: KotlinClass? = KotlinClass()

  fun m() {}

  @MyAnnotation(args = ["me", "thod"])
  fun annotatedMethod() {}


  companion object {

    @MyAnnotation(argsNoDefault = ["xyz"])
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

class SubClass: KotlinClass() {

}