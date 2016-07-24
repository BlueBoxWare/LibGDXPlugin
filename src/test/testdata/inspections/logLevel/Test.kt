package inspections.flushInsideLoop

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils

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


}