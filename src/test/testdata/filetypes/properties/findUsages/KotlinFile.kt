import com.badlogic.gdx.utils.I18NBundle
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

@GDXAssets(propertiesFiles = arrayOf("src/messages.properties" ))
var i18NBundle: I18NBundle? = null
@GDXAssets(propertiesFiles = [ "src/doesnotexist.properties" ])
var i18NBundle2: I18NBundle? = null
@GDXAssets(propertiesFiles = [ "src/extra.properties", "src/messages.properties" ])
var i18NBundle3: I18NBundle? = null
@GDXAssets(propertiesFiles = arrayOf( "src/extra.properties", "src/test.properties" ))
var i18NBundle4: I18NBundle? = null

fun f() {

  I18NBundle().get("noTranslation")
  I18NBundle().format("noTranslation", "", Any())
  I18NBundle().format("not", "noTranslation", Any())

  i18NBundle.get("noTranslation")
  i18NBundle2.get("noTranslation")
  i18NBundle3.format("noTranslation", "")
  i18NBundle3.format("", "noTranslation")
  i18NBundle4.get("noTranslation")

  i18NBundle4.format("spain", "spain")
  I18NBundle().get("spain")

}