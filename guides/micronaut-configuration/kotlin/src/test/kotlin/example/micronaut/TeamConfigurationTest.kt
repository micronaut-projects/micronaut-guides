package example.micronaut

import io.micronaut.context.ApplicationContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Arrays
import java.util.function.Consumer

class TeamConfigurationTest {
    //tag::teamConfigSpecNoBuilder[]
    @Test
    fun testTeamConfiguration() {
        val names = Arrays.asList("Nirav Assar", "Lionel Messi")
        val items: MutableMap<String, Any> = HashMap()
        items["team.name"] = "evolution"
        items["team.color"] = "green"
        items["team.player-names"] = names

        val ctx = ApplicationContext.run(items) // <1>
        val teamConfiguration = ctx.getBean(TeamConfiguration::class.java)

        assertEquals("evolution", teamConfiguration.name)
        assertEquals("green", teamConfiguration.color)
        assertEquals(names.size, teamConfiguration.playerNames!!.size)
        names.forEach(Consumer { name: String? -> assertTrue(teamConfiguration.playerNames!!.contains(name!!)) })

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
        val names = Arrays.asList("Nirav Assar", "Lionel Messi")
        val items: MutableMap<String, Any> = HashMap()
        items["team.name"] = "evolution"
        items["team.color"] = "green"
        items["team.team-admin.manager"] = "Jerry Jones" // <1>
        items["team.team-admin.coach"] = "Tommy O'Neill"
        items["team.team-admin.president"] = "Mark Scanell"
        items["team.player-names"] = names

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
