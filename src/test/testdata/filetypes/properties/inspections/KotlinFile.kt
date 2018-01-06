import com.badlogic.gdx.utils.I18NBundle
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets

@GDXAssets(propertiesFiles = ["src/messages.properties"])
var i18NBundle: I18NBundle = I18NBundle()
@GDXAssets(propertiesFiles = arrayOf( "src/doesnotexist.properties" ))
var i18NBundle2: I18NBundle = I18NBundle()
@GDXAssets(propertiesFiles = arrayOf( "src/extra.properties", "src/messages.properties" ))
var i18NBundle3: I18NBundle = I18NBundle()
@GDXAssets(propertiesFiles = ["src/extra.properties", "src/test.properties" ])
var i18NBundle4: I18NBundle = I18NBundle()

fun f() {

  I18NBundle().get("noTranslation")
  I18NBundle().format("noTranslation", "", Any())

  i18NBundle.get("noTranslation")
  i18NBundle2.get("noTranslation")
  i18NBundle3.format("germanTranslation", "")

  i18NBundle4.format("spain", "spain")
  I18NBundle().get("spain")

  i18NBundle4.get(<error>"noTranslation"</error>)
  I18NBundle().format(<error>"not"</error>, "noTranslation", Any())
  i18NBundle3.format(<error>""</error>, "noTranslation")
  i18NBundle2.get("")
  I18NBundle().get(<error>"not"</error>)

}