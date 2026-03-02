/*
 * Copyright 2026 Blue Box Ware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution
import com.badlogic.gdx.ai.utils.random.IntegerDistribution
import org.example.Dog

class KotlinBarkTask : LeafTask<Dog>() {

    @TaskAttribute
    @JvmField
    public var times: IntegerDistribution = ConstantIntegerDistribution.ONE

    private var t: Int = 3

    override fun execute(): Status? {
    }

    override fun copyTo(task: Task<Dog?>?): Task<Dog?>? {
    }


}
