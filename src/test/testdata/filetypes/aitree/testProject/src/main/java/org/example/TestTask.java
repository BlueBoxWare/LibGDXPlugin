package org.example;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.IntegerDistribution;

public class TestTask extends LeafTask<Dog> {

    @TaskAttribute
    public IntegerDistribution a1 = ConstantIntegerDistribution.ONE;

    @TaskAttribute
    public IntegerDistribution a2 = ConstantIntegerDistribution.ONE;

    @TaskAttribute
    public IntegerDistribution a3 = ConstantIntegerDistribution.ONE;

    public IntegerDistribution a4 = ConstantIntegerDistribution.ONE;

    @Override
    public Status execute() {
        return null;
    }

    @Override
    protected Task<Dog> copyTo(Task<Dog> task) {
        return null;
    }
}
