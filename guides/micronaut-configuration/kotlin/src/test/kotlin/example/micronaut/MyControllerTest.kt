package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Arrays
import java.util.function.Consumer
import jakarta.inject.Inject

@MicronautTest
class MyControllerTest(@Client("/") val client: HttpClient) {

    @Test
    fun testMyTeam() {
        val teamConfiguration = client.toBlocking()
                .retrieve(HttpRequest.GET<Any>("/my/team"), TeamConfiguration::class.java)
        assertEquals("Steelers", teamConfiguration.name)
        assertEquals("Black", teamConfiguration.color)
        val expectedPlayers = Arrays.asList("Mason Rudolph", "James Connor")
        assertEquals(expectedPlayers.size, teamConfiguration.playerNames!!.size)
        expectedPlayers.forEach(Consumer { name: String? -> assertTrue(teamConfiguration.playerNames!!.contains(name!!)) })
    }

    @Test
    fun testMyStadium() {
        val conf = client.toBlocking()
                .retrieve(HttpRequest.GET<Any>("/my/stadium"), StadiumConfiguration::class.java)
        assertEquals("Pittsburgh", conf.city)
        assertEquals(35000, conf.size)
    }
}
