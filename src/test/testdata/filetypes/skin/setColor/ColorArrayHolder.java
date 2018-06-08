import com.badlogic.gdx.graphics.Color;

class ColorArrayHolder {

  public Color[] colors;
  public Color[][] m<caret>oreColors;

  public ColorArrayHolder[] m;

  void m() {
    new com.badlogic.gdx.utils.Json().addClassTag("Tag", com.badlogic.gdx.graphics.Color.class)
  }

}