package main;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Test1 {

  TiledMap map1;

  <warning>static TiledMap map2;</warning>

  void test() {
    TiledMap map3;
  }
}
