package org.example

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import org.NamedObject

class ThingDoer : LeafTask<NamedObject>() {

    @TaskAttribute(required = true)
    @JvmField
    public var string1: String = ""

    @TaskAttribute(required = true)
    @JvmField
    public var string2: String = ""

    @JvmField
    public var string3: String = ""

    override fun execute(): Status = Status.SUCCEEDED

    override fun copyTo(task: Task<NamedObject?>?): Task<NamedObject?>? = task

}
