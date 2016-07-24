/*
 * Copyright 2016 Blue Box Ware
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

import com.gmail.blueboxware.libgdxplugin.inspections.utils.compareVersionStrings
import junit.framework.TestCase

class MiscTests: TestCase() {

  fun testVersionStringComparison() {

    val tests = setOf(
        "1.0" to "0.9",
        "0.99" to "0.9",
        "1.75" to "1.8.9",
        "1.0.0.1" to "1.0",
        "9.9" to "9.8.9"
    )

    for (pair in tests.iterator()) {
      assertEquals("${pair.first} <=> ${pair.second}", 1, compareVersionStrings(pair.first, pair.second))
      assertEquals("${pair.second} <=> ${pair.first}", -1, compareVersionStrings(pair.second, pair.first))
    }

    assertEquals(0, compareVersionStrings("1.1.9", "1.1.9"))
    assertEquals(0, compareVersionStrings("0.99", "0.99"))
  }



}
