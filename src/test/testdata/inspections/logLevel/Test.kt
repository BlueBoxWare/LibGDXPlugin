package inspections.flushInsideLoop

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Logger

fun main() {
  <warning>Gdx.app.logLevel = Application.LOG_DEBUG</warning>
  <warning>Gdx.app.logLevel = 3</warning>
  <warning>Gdx.app.logLevel = 4</warning>
  Gdx.app.logLevel = 0
  <warning>Gdx.app.logLevel = Application.LOG_INFO</warning>
  Gdx.app.logLevel = Application.LOG_ERROR

  Gdx.app.<warning>setLogLevel(3)</warning>
  Gdx.app.setLogLevel(Application.LOG_ERROR)
  Gdx.app.<warning>setLogLevel(Application.LOG_DEBUG)</warning>
  Gdx.app.setLogLevel(0)

  val a = Gdx.app


  <warning>a.logLevel = 3</warning>

  Logger("").<warning>setLevel(3)</warning>
  Logger("").<warning>setLevel(Logger.DEBUG)</warning>
  <warning>Logger("").level = 3</warning>
  <warning>Logger("").level = Logger.INFO</warning>
  Logger("").level = Logger.ERROR

  <warning>AssetManager().getLogger().level = 3</warning>
  <warning>AssetManager().logger.level = Logger.DEBUG</warning>
  AssetManager().logger.level = 1

}