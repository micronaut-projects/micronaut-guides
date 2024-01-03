/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.inject.qualifiers.Qualifiers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StadiumConfigurationTest {
    @Test
    fun testStadiumConfiguration() {
        val items: MutableMap<String, Any> = HashMap()
        items["stadium.fenway.city"] = "Boston" // <1>
        items["stadium.fenway.size"] = 60000
        items["stadium.wrigley.city"] = "Chicago" // <1>
        items["stadium.wrigley.size"] = 45000

        val ctx = ApplicationContext.run(items)

        // <2>
        val fenwayConfiguration = ctx.getBean(StadiumConfiguration::class.java, Qualifiers.byName("fenway"))
        val wrigleyConfiguration = ctx.getBean(StadiumConfiguration::class.java, Qualifiers.byName("wrigley"))

        assertEquals("fenway", fenwayConfiguration.name)
        assertEquals(60000, fenwayConfiguration.size)
        assertEquals("wrigley", wrigleyConfiguration.name)
        assertEquals(45000, wrigleyConfiguration.size)

        ctx.close()
    }
}
