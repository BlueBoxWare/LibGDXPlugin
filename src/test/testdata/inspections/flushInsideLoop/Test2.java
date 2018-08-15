package flushInsideLoop;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Test2 {

    void test() {
        Clazz c7 = new Clazz();

        while (MathUtils.randomBoolean()) {
            <warning>c7.getI()</warning>;
            <warning>c7.bla()</warning>;
            c7.setI(3);
            <warning>c7.setJ(3)</warning>;
            c7.getJ();

            Clazz1 clazz1 = <warning>new Clazz1()</warning>;

            Clazz2 clazz2 = new Clazz2();
            Clazz2 clazz2a = <warning>new Clazz2("dfs")</warning>;
        }
        
    }

}
