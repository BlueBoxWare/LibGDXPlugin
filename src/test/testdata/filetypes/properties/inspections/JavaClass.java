import com.badlogic.gdx.utils.I18NBundle;
import com.gmail.blueboxware.libgdxplugin.annotations.GDXAssets;

class JavaClass {

  @GDXAssets(propertiesFiles = {"src/messages.properties"})
  I18NBundle i18NBundle;
  @GDXAssets(propertiesFiles = {"src/doesnotexist.properties"})
  I18NBundle i18NBundle2;
  @GDXAssets(propertiesFiles = {"src/extra.properties", "src/messages.properties"})
  I18NBundle i18NBundle3;
  @GDXAssets(propertiesFiles = {"src/extra.properties", "src/test.properties"})
  I18NBundle i18NBundle4;

  void m() {
    new I18NBundle().get("noTranslation");
    new I18NBundle().format("noTranslation", "", new Object());

    i18NBundle.get("noTranslation");
    i18NBundle2.get("noTranslation");
    i18NBundle3.get("noTranslation");
    i18NBundle4.get("something");

    i18NBundle4.get("spain");
    new I18NBundle().format("spain", "spain");

    new I18NBundle().get(<error>"noT"</error>);
    new I18NBundle().format(<error>"noT"</error>, "", "something", new Object());

    i18NBundle.get(<error>"nothing"</error>);
    i18NBundle2.get("abc");
    i18NBundle3.get(<error>"default"</error>);
    i18NBundle3.format("french.Only", "");
    i18NBundle4.format(<error>""</error>, "something", "spain");

    new I18NBundle().format(<error>"spin"</error>, "spain");

  }

}