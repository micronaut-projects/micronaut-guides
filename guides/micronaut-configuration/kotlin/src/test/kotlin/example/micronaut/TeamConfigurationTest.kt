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

class TeamConfigurationTest {
    //tag::teamConfigSpecNoBuilder[]
    @Test
    fun testTeamConfiguration() {
        val names = listOf("Nirav Assar", "Lionel Messi")
        val items = mapOf("team.name" to "evolution",
            "team.color" to "green",
            "team.player-names" to names)

        val ctx = ApplicationContext.run(items) // <1>
        val teamConfiguration = ctx.getBean(TeamConfiguration::class.java)

        assertEquals("evolution", teamConfiguration.name)
        assertEquals("green", teamConfiguration.color)
        assertEquals(names.size, teamConfiguration.playerNames!!.size)

        names.forEach {
            assertTrue(teamConfiguration.playerNames!!.contains(it))
        }

        ctx.close()
    }

    //end::teamConfigSpecNoBuilder[]
    @Test
    fun testBuilderPatternPlainUsage() {
        val teamAdmin = TeamAdmin.Builder().withManager("Nirav")
                .withCoach("Tommy")
                .withPresident("Mark").build()
        assertEquals("Nirav", teamAdmin.manager)
        assertEquals("Tommy", teamAdmin.coach)
        assertEquals("Mark", teamAdmin.president)
    }

    //tag::teamConfigSpecBuilder[]
    @Test
    fun testTeamConfigurationBuilder() {
        val names = listOf("Nirav Assar", "Lionel Messi")
        val items = mapOf("team.name" to "evolution",
            "team.color" to "green",
            "team.team-admin.manager" to "Jerry Jones", // <1>
            "team.team-admin.coach" to "Tommy O'Neill",
            "team.team-admin.president" to "Mark Scanell",
            "team.player-names" to names)

        val ctx = ApplicationContext.run(items)
        val teamConfiguration = ctx.getBean(TeamConfiguration::class.java)
        val teamAdmin = teamConfiguration.builder.build() // <2>

        assertEquals("evolution", teamConfiguration.name)
        assertEquals("green", teamConfiguration.color)
        assertEquals("Nirav Assar", teamConfiguration.playerNames!![0])
        assertEquals("Lionel Messi", teamConfiguration.playerNames!![1])

        // check the builder has values set
        assertEquals("Jerry Jones", teamConfiguration.builder.manager)
        assertEquals("Tommy O'Neill", teamConfiguration.builder.coach)
        assertEquals("Mark Scanell", teamConfiguration.builder.president)

        // check the object can be built
        assertEquals("Jerry Jones", teamAdmin.manager) // <3>
        assertEquals("Tommy O'Neill", teamAdmin.coach)
        assertEquals("Mark Scanell", teamAdmin.president)
        ctx.close()
    } //end::teamConfigSpecBuilder[]
}
