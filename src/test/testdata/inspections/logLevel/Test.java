package inspections.flushInsideLoop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Logger;

class AM extends AssetManager {

}

public class Test {

    Application a;

    void test() {

        Application b = Gdx.app;

        <warning>b.setLogLevel(Application.LOG_DEBUG)</warning>;
        <warning>a.setLogLevel(3)</warning>;

        <warning>Gdx.app.setLogLevel(Application.LOG_INFO)</warning>;
        Gdx.app.setLogLevel(0);
        Gdx.app.setLogLevel(Application.LOG_ERROR);

        <warning>new Logger("").setLevel(Logger.DEBUG)</warning>;
        <warning>new Logger("").setLevel(3)</warning>;

        <warning>new AssetManager().getLogger().setLevel(3)</warning>;
        <warning>new AM().getLogger().setLevel(Logger.INFO)</warning>;

    }

    void m(Application c) {
        <warning>c.setLogLevel(3)</warning>;
    }

}