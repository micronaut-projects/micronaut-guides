package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*

@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest
internal class MicronautguideTest : TestPropertyProvider {

    @Inject
    @field:Client("/")
    lateinit var client : HttpClient

    @Inject
    lateinit var demoConsumer: DemoConsumer

    @Test
    fun testItWorks() {
        var messageCount = demoConsumer.getMessageCount()
        Assertions.assertTrue(messageCount == 0)

        client.toBlocking().exchange<Any, Any>(HttpRequest.POST("/demo", emptyMap<Any, Any>()))
        messageCount = demoConsumer.getMessageCount()
        while (messageCount == 0) {
            messageCount = demoConsumer.getMessageCount()
        }
        Assertions.assertTrue(messageCount == 1)
    }

    @NonNull
    override fun getProperties(): Map<String, String> {
        return LocalStackUtil.properties
    }

    @AfterAll
    fun afterAll() {
        LocalStackUtil.close()
    }

}