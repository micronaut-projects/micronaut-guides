package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*
import java.util.function.Consumer
import javax.inject.Inject

@MicronautTest
class MyControllerTest {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testMyTeam() {
        val teamConfiguration = client.toBlocking()
                .retrieve(HttpRequest.GET<Any>("/my/team"), TeamConfiguration::class.java)
        Assertions.assertEquals("Steelers", teamConfiguration.name)
        Assertions.assertEquals("Black", teamConfiguration.color)
        val expectedPlayers = Arrays.asList("Mason Rudolph", "James Connor")
        Assertions.assertEquals(expectedPlayers.size, teamConfiguration.playerNames!!.size)
        expectedPlayers.forEach(Consumer { name: String? -> Assertions.assertTrue(teamConfiguration.playerNames!!.contains(name!!)) })
    }

    @Test
    fun testMyStadium() {
        val conf = client.toBlocking()
                .retrieve(HttpRequest.GET<Any>("/my/stadium"), StadiumConfiguration::class.java)
        Assertions.assertEquals("Pittsburgh", conf.city)
        Assertions.assertEquals(35000, conf.size)
    }
}