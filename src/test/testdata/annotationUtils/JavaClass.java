public class JavaClass {

  @MyAnnotation(args = {"Foo", "Bar"}, arg = {"arg"}, argNoDefault = "")
  public static JavaClass INSTANCE;

  @MyAnnotation(args = {"one", "two"}, argNoDefault = "three")
  public JavaClass javaClass;

  public SubClass subClass;
  @MyAnnotation(args = {"sub"})
  public SubClass annoSubClass;

  public String myString = "foo";

  @MyAnnotation(arg = {"t"})
  String t;

  public void m() {

  }

  public JavaClass f() {

  }

  public static JavaClass g() {

  }

  @MyAnnotation(args = {"sub"})
  public void annotatedMethod() {

  }

  public static class SubClass extends JavaClass {
    void m() {

    }
  }

  static String staticMethod() { }

  static String staticAnnotatedMethod() { }
}
