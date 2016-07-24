import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences

val prefs: Preferences by lazy {
  <warning>Gdx.app.getPreferences("a").putBoolean("a", true)</warning>
}

fun fun1() {
  val preferences = Gdx.app.getPreferences("sdf")
  <warning>preferences.putBoolean("fds", true)</warning>
}

fun fun1a() {
  val preferences = Gdx.app.getPreferences("sdf")
  preferences.putBoolean("fds", true)

  if (true) {
    preferences.flush()
  }

  preferences.flush()
}

fun fun2() {
  val p = Gdx.app.getPreferences("a")
  p.remove("dfs")
  <warning>p.putBoolean("a", true)</warning>

}

fun fun3() {
  val p = Gdx.app.getPreferences("a")

  for (i in listOf(1,2,3)) <warning>p.putString("a", "b")</warning>

  listOf(1,2,3).map { it -> <warning>p.putInteger("a", it)</warning> }

  listOf(1,2,3).map { it -> p.putInteger("a", it); p.flush() }
}

fun fun3a() {
  val p = Gdx.app.getPreferences("a")

  for (i in listOf(1,2,3)) p.putString("a", "b")

  listOf(1,2,3).map { it -> <warning>p.putInteger("a", it)</warning> }

  p.flush()

  listOf(1,2,3).map { it -> p.putInteger("a", it); p.flush() }
}

fun fun4() {
  for (i in listOf(1,2,3)) {
    Gdx.app.getPreferences("a").putString("a", "b")
    Gdx.app.getPreferences("a").flush();
    <warning>Gdx.app.getPreferences("a").putBoolean("a", true)</warning>
  }
}

fun fun5() {
  prefs.let {
    <warning>it.putBoolean("a", true)</warning>
  }

  prefs.flush()
}

fun fun6() {
  prefs.let {
    it.putBoolean("a", true)
    it.flush()
  }
}


class TestPrefs {
  val p = Gdx.app.getPreferences("a")
  var c: (() -> Unit)? = null

  init {
    <warning>p.putBoolean("a", true)</warning>
  }

  fun fun1() {
    p.remove("a")
    Gdx.app.getPreferences("b").flush()
    <warning>p.remove("a")</warning>
  }

  fun fun2() {
    setCallback { <warning>Gdx.app.getPreferences("a").putBoolean("a", true)</warning> }
    Gdx.app.getPreferences("f").flush()
  }

  fun fun3() {
    setCallback { Gdx.app.getPreferences("a").putBoolean("a", true); Gdx.app.getPreferences("a").flush() }
  }

  fun fun4() {
    c = {
      <warning>Gdx.app.getPreferences("a").remove("a")</warning>
    }
  }

  fun setCallback(f: ()->Unit) {
    c = f
  }

}


