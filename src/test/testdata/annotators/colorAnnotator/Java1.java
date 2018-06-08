import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

public class Java1 {

  @GDXAssets(skinFiles = {"src/libgdx.skin"})
  Skin skin;

  static final String c1 = "ff0000";
  static final String c2 = <weak_warning descr="#00ff00ff">"#00ff00"</weak_warning>;
  static final Color cf3 = <weak_warning descr="#0000ffff">Color.valueOf("0000ffff")</weak_warning>;
  static final Color c4 = Color.valueOf(<weak_warning descr="#00ff00ff">c2</weak_warning>);
  static final Color c5 = Color.BLUE;
  static final Color c6 = <weak_warning descr="#ff0000ff">Color.valueOf(c1)</weak_warning>;
  static final Color c7 = <weak_warning descr="#00001168">new Color(4456)</weak_warning>;
  static final Color c8 = <weak_warning descr="#ff0000ff">new Color(0xff0000ff)</weak_warning>; static final Color sfdwe = <weak_warning descr="#00001168">c7</weak_warning>;
  static final Color c9 = <weak_warning descr="#ff00ffff">new Color(1, 0, 1, 1)</weak_warning>;

  final String cc1 = "ff0000";
  final String cc2 = <weak_warning descr="#00ff00ff">"#00ff00"</weak_warning>;
  final Color cc3 = <weak_warning descr="#0000ffff">Color.valueOf("0000ffff")</weak_warning>;
  final Color cc4 = Color.valueOf(<weak_warning descr="#00ff00ff">c2</weak_warning>);
  final Color cc5 = Color.BLUE;
  final Color cc6 = <weak_warning descr="#ff0000ff">Color.valueOf(c1)</weak_warning>;
  final Color cc7 = <weak_warning descr="#00001168">new Color(4456)</weak_warning>;
  final Color cc8 = <weak_warning descr="#ff0000ff">new Color(0xff0000ff)</weak_warning>;
  final Color cc9 = <weak_warning descr="#ff00ffff">new Color(1, 0, 1, 1)</weak_warning>;

  final String resource = "red";
  final Color ccc1 = <weak_warning descr="#ff0000ff">skin.getColor("red")</weak_warning>;
  final Color ccca1 = <weak_warning descr="#ff0000ff">skin.getColor("taggedColor1")</weak_warning>;
  final Color ccca2 = <weak_warning descr="#ff00ffff">skin.getColor("taggedColor2")</weak_warning>;
  final Color ccca3 = <weak_warning descr="#808000ff">skin.getColor("taggedColor3")</weak_warning>;
  final Color ccc2 = <weak_warning descr="#ff0000ff">skin.get("red", Color.class)</weak_warning>;
  final Drawable ccca = skin.get("red", Drawable.class);
  final Color ccc4 = <weak_warning descr="#ff0000ff">skin.optional(resource, Color.class)</weak_warning>;
  final Color ccc5 = <weak_warning descr="#ff0000ff">ccc1</weak_warning>;
  final Color ccc6 = <weak_warning descr="#ff0000ff">ccc2</weak_warning>;
  final Color ccc8 = <weak_warning descr="#ff0000ff">ccc4</weak_warning>;

  void b() {
    final Color ccccc1 = <weak_warning descr="#ff0000ff">ccc5</weak_warning>;
    final Color ccccc2 = <weak_warning descr="#ff0000ff">ccc6</weak_warning>;
    final Color ccccc4 = <weak_warning descr="#ff0000ff">ccc8</weak_warning>;

    Color zza1 = <weak_warning descr="#ff0000ff">ccca1</weak_warning>;
    Color zza2 = <weak_warning descr="#ff00ffff">ccca2</weak_warning>;
    Color zza3 = <weak_warning descr="#808000ff">ccca3</weak_warning>;

    new com.badlogic.gdx.utils.Json().addClassTag("Tag", com.badlogic.gdx.graphics.Color.class);
  }

  public static class C {

    public final static String c1 = "ff0000";
    final String c2 = <weak_warning descr="#00ff00ff">"#00ff00"</weak_warning>;
    final Color c3 = <weak_warning descr="#0000ffff">Color.valueOf("0000ffff")</weak_warning>;
    final Color c4 = Color.valueOf(<weak_warning descr="#00ff00ff">c2</weak_warning>);
    final Color c5 = Color.BLUE;
    final Color c6 = <weak_warning descr="#ff0000ff">Color.valueOf(c1)</weak_warning>;
    final Color c7 = <weak_warning descr="#00001168">new Color(4456)</weak_warning>;
    final Color c8 = <weak_warning descr="#ff0000ff">new Color(0xff0000ff)</weak_warning>;
    final Color c9 = <weak_warning descr="#ff00ffff">new Color(1, 0, 1, 1)</weak_warning>;

    final String s1 = Java1.c1;
    final String s2 = <weak_warning descr="#00ff00ff">Java1.c2</weak_warning>;
    final Color cc1 = <weak_warning descr="#0000ffff">Java1.cf3</weak_warning>;
    final Color cc2 = <weak_warning descr="#00ff00ff">Java1.c4</weak_warning>;
    final Color cc3 = Java1.c5;
    final Color cc4 = <weak_warning descr="#ff0000ff">Java1.c6</weak_warning>;
    final Color cc5 = <weak_warning descr="#00001168">Java1.c7</weak_warning>;
    final Color cc6 = <weak_warning descr="#ff0000ff">Java1.c8</weak_warning>;
    final Color cc7 = <weak_warning descr="#ff00ffff">Java1.c9</weak_warning>;

    Color ccc1 = <weak_warning descr="#0000ffff">cc1</weak_warning>;
    Color ccc2 = <weak_warning descr="#00ff00ff">cc2</weak_warning>;
    Color ccc3 = cc3;
    Color ccc4 = <weak_warning descr="#ff0000ff">cc4</weak_warning>;
    Color ccc5 = <weak_warning descr="#ff0000ff">cc6</weak_warning>;
    Color ccc6 = <weak_warning descr="#ff00ffff">cc7</weak_warning>;

  }

  static void f() {
    final Color dc1 = <weak_warning descr="#ff0000ff">Color.valueOf(Java1.c1)</weak_warning>;

    Color ddfGedf1 = <weak_warning descr="#ff0000ff">dc1</weak_warning>;

    Color c1 = <weak_warning descr="#0000ffff">(new Java1.C()).cc1</weak_warning>;
    Color c2 = <weak_warning descr="#00ff00ff">(new Java1.C()).cc2</weak_warning>;
    Color c3 = (new Java1.C()).cc3;
    Color c4 = <weak_warning descr="#ff0000ff">(new Java1.C()).cc4</weak_warning>;
    Color c5 = <weak_warning descr="#00001168">(new Java1.C()).cc5</weak_warning>;
    Color c6 = <weak_warning descr="#ff0000ff">(new Java1.C()).cc6</weak_warning>;
    Color c7d = <weak_warning descr="#ff00ffff">(new Java1.C()).cc7</weak_warning>;

    Color zz1 = <weak_warning descr="#ff0000ff">Color.valueOf(new C().c1)</weak_warning>;
    Color zz2 = Color.valueOf(<weak_warning descr="#00ff00ff">new C().c2</weak_warning>);

  }

}
