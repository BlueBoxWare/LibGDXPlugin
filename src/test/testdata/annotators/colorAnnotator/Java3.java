/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.graphics.Color;

/** A general purpose class containing named colors that can be changed at will. For example, the markup language defined by the
 * {@code BitmapFontCache} class uses this class to retrieve colors and the user can define his own colors.
 *
 * @author davebaol */
final class Colors {

  private static final ObjectMap<String, Color> map = new ObjectMap<String, Color>();

  static {
    reset();
  }

  /** Returns the color map. */
  public static ObjectMap<String, Color> getColors () {
    return map;
  }

  /** Convenience method to lookup a color by {@code name}. The invocation of this method is equivalent to the expression
   * {@code Colors.getColors().get(name)}
   *
   * @param name the name of the color
   * @return the color to which the specified {@code name} is mapped, or {@code null} if there was no mapping for {@code name}
   *         . */
  public static Color get (String name) {
    return map.get(name);
  }

  /** Convenience method to add a {@code color} with its {@code name}. The invocation of this method is equivalent to the
   * expression {@code Colors.getColors().put(name, color)}
   *
   * @param name the name of the color
   * @param color the color
   * @return the previous {@code color} associated with {@code name}, or {@code null} if there was no mapping for {@code name}
   *         . */
  public static Color put (String name, Color color) {
    return map.put(name, color);
  }

  /** Resets the color map to the predefined colors. */
  public static void reset () {
    map.clear();
    map.put("CLEAR", <weak_warning descr="#00000000">Color.CLEAR</weak_warning>);
    map.put("BLACK", <weak_warning descr="#000000ff">Color.BLACK</weak_warning>);

    map.put("WHITE", <weak_warning descr="#ffffffff">Color.WHITE</weak_warning>);
    map.put("LIGHT_GRAY", <weak_warning descr="#bfbfbfff">Color.LIGHT_GRAY</weak_warning>);
    map.put("GRAY", <weak_warning descr="#7f7f7fff">Color.GRAY</weak_warning>);
    map.put("DARK_GRAY", <weak_warning descr="#3f3f3fff">Color.DARK_GRAY</weak_warning>);

    map.put("BLUE", <weak_warning descr="#0000ffff">Color.BLUE</weak_warning>);
    map.put("NAVY", <weak_warning descr="#000080ff">Color.NAVY</weak_warning>);
    map.put("ROYAL", <weak_warning descr="#4169e1ff">Color.ROYAL</weak_warning>);
    map.put("SLATE", <weak_warning descr="#708090ff">Color.SLATE</weak_warning>);
    map.put("SKY", <weak_warning descr="#87ceebff">Color.SKY</weak_warning>);
    map.put("CYAN", <weak_warning descr="#00ffffff">Color.CYAN</weak_warning>);
    map.put("TEAL", <weak_warning descr="#008080ff">Color.TEAL</weak_warning>);

    map.put("GREEN", <weak_warning descr="#00ff00ff">Color.GREEN</weak_warning>);
    map.put("CHARTREUSE", <weak_warning descr="#7fff00ff">Color.CHARTREUSE</weak_warning>);
    map.put("LIME", <weak_warning descr="#32cd32ff">Color.LIME</weak_warning>);
    map.put("FOREST", <weak_warning descr="#228b22ff">Color.FOREST</weak_warning>);
    map.put("OLIVE", <weak_warning descr="#6b8e23ff">Color.OLIVE</weak_warning>);

    map.put("YELLOW", <weak_warning descr="#ffff00ff">Color.YELLOW</weak_warning>);
    map.put("GOLD", <weak_warning descr="#ffd700ff">Color.GOLD</weak_warning>);
    map.put("GOLDENROD", <weak_warning descr="#daa520ff">Color.GOLDENROD</weak_warning>);
    map.put("ORANGE", <weak_warning descr="#ffa500ff">Color.ORANGE</weak_warning>);

    map.put("BROWN", <weak_warning descr="#8b4513ff">Color.BROWN</weak_warning>);
    map.put("TAN", <weak_warning descr="#d2b48cff">Color.TAN</weak_warning>);
    map.put("FIREBRICK", <weak_warning descr="#b22222ff">Color.FIREBRICK</weak_warning>);

    map.put("RED", <weak_warning descr="#ff0000ff">Color.RED</weak_warning>);
    map.put("SCARLET", <weak_warning descr="#ff341cff">Color.SCARLET</weak_warning>);
    map.put("CORAL", <weak_warning descr="#ff7f50ff">Color.CORAL</weak_warning>);
    map.put("SALMON", <weak_warning descr="#fa8072ff">Color.SALMON</weak_warning>);
    map.put("PINK", <weak_warning descr="#ff69b4ff">Color.PINK</weak_warning>);
    map.put("MAGENTA", <weak_warning descr="#ff00ffff">Color.MAGENTA</weak_warning>);

    map.put("PURPLE", <weak_warning descr="#a020f0ff">Color.PURPLE</weak_warning>);
    map.put("VIOLET", <weak_warning descr="#ee82eeff">Color.VIOLET</weak_warning>);
    map.put("MAROON", <weak_warning descr="#b03060ff">Color.MAROON</weak_warning>);
  }

  private Colors () {
  }

}
