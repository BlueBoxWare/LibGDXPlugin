package inspections.flushInsideLoop;

import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class Test3 {

    public Test3() {

    }

    public Test3(SpriteBatch batch) {
        batch.flush();
    }

    public Test3(int i) {
        new SpriteBatch().setTransformMatrix(new Matrix4());
    }

    public Test3(float f) {
        new CpuSpriteBatch().setTransformMatrix(new Matrix4());
    }

    public static void sMethod() {
        new SpriteBatch().flush();
    }

    public void method1() {
        new SpriteBatch().setTransformMatrix(new Matrix4());
    }

    public void method2(SpriteBatch batch) {
        batch.setTransformMatrix(null);
    }

    public void method3() { new CpuSpriteBatch().setTransformMatrix(new Matrix4());}


}