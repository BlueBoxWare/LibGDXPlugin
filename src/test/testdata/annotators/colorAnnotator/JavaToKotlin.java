import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class JavaToKotlin {

  static final String c1 = "ff0000";
  static final String c2 = "#00ff00";
  static final Color cf3 = Color.valueOf("0000ffff");
  static final Color c4 = Color.valueOf(c2);
  static final Color c5 = Color.BLUE;
  static final Color c6 = Color.valueOf(c1);
  static final Color c7 = new Color(4456);
  static final Color c8 = new Color(0xff0000ff); static final Color sfdwe = c7;
  static final Color c9 = new Color(1, 0, 1, 1);
  static final Color c10 = Colors.get("foobar");
  static final Color c11 = Colors.getColors().get("some");

  public static class C {
    public final static String c1 = "ff0000";
  }

  void m() {
    Colors.put("foobar", Color.YELLOW);
    Colors.getColors().put("some", Color.RED);
  }

}