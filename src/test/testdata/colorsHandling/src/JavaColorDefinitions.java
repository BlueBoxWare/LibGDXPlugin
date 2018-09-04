import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class JavaColorDefinitions {

  void m() {
    Colors.put("singlePutJava", Color.PURPLE);
    Colors.put("multiJava", Color.GREEN);
    Colors.put("multiJava", Color.CORAL);
    Colors.getColors().put("singleGetColorsJava", Color.CYAN);

    Colors.get("c");
    Colors.getColors().get("c");
  }
}
