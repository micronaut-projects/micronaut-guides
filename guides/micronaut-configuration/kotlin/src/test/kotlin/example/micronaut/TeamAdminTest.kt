package example.micronaut;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TeamAdminTest {
    @Test
    fun  aTeamAdminIsConstructedWithTheBuilder() {
        val teamAdmin = TeamAdmin.Builder().withManager("Nirav")
                .withCoach("Tommy")
                .withPresident("Mark").build()
        assertEquals("Nirav", teamAdmin.manager)
        assertEquals("Tommy", teamAdmin.coach)
        assertEquals("Mark", teamAdmin.president)
    }
}
