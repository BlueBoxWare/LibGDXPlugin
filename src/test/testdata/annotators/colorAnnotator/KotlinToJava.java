import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class KotlinToJava {

  public static void main(String ps[]) {

    String ff1 = <weak_warning descr="#ffff00ff">KotlinToJavaKt.constC</weak_warning>;

    final Color ccc = <weak_warning descr="#ff00ffff">Color.valueOf(KotlinToJavaKt.getS())</weak_warning>;
    String c1 = <weak_warning descr="#ff0000ff">KotlinToJavaKt.getC1()</weak_warning>;
    final Color c2 = <weak_warning descr="#00006443">new C().getC2()</weak_warning>;
    Color c3 = <weak_warning descr="#ff00ffff">ccc</weak_warning>;
    Color c4 = <weak_warning descr="#00006443">c2</weak_warning>;

    Color c5 = <weak_warning descr="#ff0000ff">new C().getC3()</weak_warning>;
    Color c6 = <weak_warning descr="#ffff00ff">Colors.get("foobar")</weak_warning>;

  }

}