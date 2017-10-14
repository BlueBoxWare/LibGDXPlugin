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

  String s = i18NBundle.get("oldName");

  void m() {
    new I18NBundle().get("oldName");
    i18NBundle.format("oldName", "", new Object());
    i18NBundle.format("oldName", "oldName", "");
    i18NBundle2.get("oldName");
    i18NBundle3.format("oldName");
    i18NBundle4.format("oldName");
  }
}