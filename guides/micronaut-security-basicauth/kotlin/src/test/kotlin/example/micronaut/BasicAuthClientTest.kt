package example.micronaut

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Base64

@MicronautTest // <1>
class BasicAuthClientTest {

    @Inject
    lateinit var appClient : AppClient // <2>

    @Test
    fun verifyBasicAuthWorks() {
        val credsEncoded = Base64.getEncoder().encodeToString("sherlock:password".toByteArray())
        val rsp = appClient.home("Basic $credsEncoded") // <3>
        assertEquals("sherlock", rsp)
    }
}
