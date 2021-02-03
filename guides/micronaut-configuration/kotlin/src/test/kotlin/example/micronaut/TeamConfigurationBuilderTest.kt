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
        val ctx = ApplicationContext.run(ApplicationContext::class.java, items) // <1>
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