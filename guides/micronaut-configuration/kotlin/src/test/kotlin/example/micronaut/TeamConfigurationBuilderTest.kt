/*
 * Copyright 2017-2023 original authors
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.function.Consumer

class TeamConfigurationBuilderTest {
    @Test
    fun anApplicationContextStartedWithConfiguration() {
        val names = listOf("Nirav Assar", "Lionel Messi")
        val items = mapOf("team.name" to "evolution",
                "team.color" to "green",
                "team.team-admin.manager" to "Jerry Jones", // <1>
                "team.team-admin.coach" to "Tommy O'Neill",
                "team.team-admin.president" to "Mark Scanell",
                "team.player-names" to names)

        val ctx = ApplicationContext.run(items) // <1>

        //`when`("TeamConfiguration is retrieved from the context") {
        val teamConfiguration = ctx.getBean(TeamConfiguration::class.java)

        //then("configuration properties are populated") {
         assertEquals("evolution", teamConfiguration.name)
         assertEquals("green", teamConfiguration.color)
         assertEquals(names.size, teamConfiguration.playerNames!!.size)
         names.forEach(Consumer { name: String? -> assertTrue(teamConfiguration.playerNames!!.contains(name)) })
                assertEquals("Jerry Jones", teamConfiguration.builder.manager)
                assertEquals("Tommy O'Neill", teamConfiguration.builder.coach)
                assertEquals("Mark Scanell", teamConfiguration.builder.president)

                val teamAdmin = teamConfiguration.builder.build() // <2>
                assertEquals("Jerry Jones", teamAdmin.manager)  // <3>
                assertEquals("Tommy O'Neill", teamAdmin.coach)
                assertEquals("Mark Scanell", teamAdmin.president)
        ctx.close()
    }
}