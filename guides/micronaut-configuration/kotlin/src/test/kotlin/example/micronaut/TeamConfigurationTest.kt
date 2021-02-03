package example.micronaut

import io.micronaut.context.ApplicationContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*
import java.util.function.Consumer
import kotlin.collections.HashMap

class TeamConfigurationTest {
    //tag::teamConfigSpecNoBuilder[]
    @Test
    fun testTeamConfiguration() {
        val names = Arrays.asList("Nirav Assar", "Lionel Messi")
        val items: MutableMap<String, Any> = HashMap()
        items["team.name"] = "evolution"
        items["team.color"] = "green"
        items["team.player-names"] = names
        val ctx = ApplicationContext.run(ApplicationContext::class.java, items) // <1>
        val teamConfiguration = ctx.getBean(TeamConfiguration::class.java)
        Assertions.assertEquals("evolution", teamConfiguration.name)
        Assertions.assertEquals("green", teamConfiguration.color)
        Assertions.assertEquals(names.size, teamConfiguration.playerNames!!.size)
        names.forEach(Consumer { name: String? -> Assertions.assertTrue(teamConfiguration.playerNames!!.contains(name!!)) })
        ctx.close()
    }

    //end::teamConfigSpecNoBuilder[]
    @Test
    fun testBuilderPatternPlainUsage() {
        val teamAdmin = TeamAdmin.Builder().withManager("Nirav")
                .withCoach("Tommy")
                .withPresident("Mark").build()
        Assertions.assertEquals("Nirav", teamAdmin.manager)
        Assertions.assertEquals("Tommy", teamAdmin.coach)
        Assertions.assertEquals("Mark", teamAdmin.president)
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
        val ctx = ApplicationContext.run(ApplicationContext::class.java, items)
        val teamConfiguration = ctx.getBean(TeamConfiguration::class.java)
        val teamAdmin = teamConfiguration.builder.build() // <2>
        Assertions.assertEquals("evolution", teamConfiguration.name)
        Assertions.assertEquals("green", teamConfiguration.color)
        Assertions.assertEquals("Nirav Assar", teamConfiguration.playerNames!![0])
        Assertions.assertEquals("Lionel Messi", teamConfiguration.playerNames!![1])

        // check the builder has values set
        Assertions.assertEquals("Jerry Jones", teamConfiguration.builder.manager)
        Assertions.assertEquals("Tommy O'Neill", teamConfiguration.builder.coach)
        Assertions.assertEquals("Mark Scanell", teamConfiguration.builder.president)

        // check the object can be built
        Assertions.assertEquals("Jerry Jones", teamAdmin.manager) // <3>
        Assertions.assertEquals("Tommy O'Neill", teamAdmin.coach)
        Assertions.assertEquals("Mark Scanell", teamAdmin.president)
        ctx.close()
    } //end::teamConfigSpecBuilder[]
}