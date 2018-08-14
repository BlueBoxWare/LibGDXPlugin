import com.gmail.blueboxware.libgdxplugin.annotations.GDXTag;

@GDXTag({"TagColor", "javaTag1", <warning>"foo"</warning>, "TagStyle"})
class Test {

  @GDXTag(value = {"TagColor", "javaTag1", <warning>"foo"</warning>, "kotlinTag2", <warning>"bar"</warning>})
  class InnerClass {}

}