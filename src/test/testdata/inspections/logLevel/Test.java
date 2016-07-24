package inspections.flushInsideLoop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class Test {

    Application a;

    void test() {

        Application b = Gdx.app;

        <warning>b.setLogLevel(Application.LOG_DEBUG)</warning>;
        <warning>a.setLogLevel(3)</warning>;

        <warning>Gdx.app.setLogLevel(Application.LOG_INFO)</warning>;
        Gdx.app.setLogLevel(0);
        Gdx.app.setLogLevel(Application.LOG_ERROR);

    }

    void m(Application c) {
        <warning>c.setLogLevel(3)</warning>;
    }

}