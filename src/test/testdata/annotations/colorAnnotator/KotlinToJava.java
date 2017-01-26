public class KotlinToJava {

  public static void main(String ps[]) {

    final com.badlogic.gdx.graphics.Color ccc = <weak_warning descr="#ff00ffff">com.badlogic.gdx.graphics.Color.valueOf(KotlinToJavaKt.getS())</weak_warning>;
    String c1 = <weak_warning descr="#ff0000ff">KotlinToJavaKt.getC1()</weak_warning>;
    final com.badlogic.gdx.graphics.Color c2 = <weak_warning descr="#00006443">new C().getC2()</weak_warning>;
    <error descr="Cannot resolve symbol 'Color'">Color</error> c3 = <weak_warning descr="#ff00ffff">ccc</weak_warning>;
    <error descr="Cannot resolve symbol 'Color'">Color</error> c4 = <weak_warning descr="#00006443">c2</weak_warning>;

  }

}