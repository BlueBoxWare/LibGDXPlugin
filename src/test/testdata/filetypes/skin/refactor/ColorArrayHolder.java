import com.badlogic.gdx.graphics.Color;

class ColorArrayHolder {

  public Color[] colors;
  public Color[][] mor<caret>eColors;

  public ColorArrayHolder[] m;

  public com.example.KotlinClass kotlin;

  void m() {
    new com.badlogic.gdx.utils.Json().addClassTag("Tag1", ColorArrayHolder.class)
    new com.badlogic.gdx.utils.Json().addClassTag("Tag2", com.example.KotlinClass.class)
  }

}