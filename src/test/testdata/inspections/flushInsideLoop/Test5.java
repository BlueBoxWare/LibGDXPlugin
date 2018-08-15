package flushInsideLoop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

class ClassA extends SpriteBatch {

}

class ClassB extends ClassA {
    @Override
    public void flush() {
        super.flush();
    }
}

class ClassC extends ClassB {

    @Override
    public void flush() {

    }

    public void myFlush() {
        super.flush();
    }

    static void test() {

        ClassA a = new ClassA();
        ClassB b = new ClassB();
        ClassC c = new ClassC();

        while (MathUtils.randomBoolean()) {
            <warning>a.flush()</warning>;
            <warning>b.setTransformMatrix(null)</warning>;
            <warning>b.flush()</warning>;
            c.flush();
            <warning>c.myFlush()</warning>;
        }


    }
}

