package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.Map;


@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest
class MicronautguideTest implements TestPropertyProvider {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    DemoConsumer demoConsumer;

    @Test
    void testItWorks() {
        int messageCount = demoConsumer.getMessageCount();
        Assertions.assertTrue(messageCount == 0);

        httpClient.toBlocking().exchange(HttpRequest.POST("/demo", Collections.emptyMap()));
        messageCount = demoConsumer.getMessageCount();
        while (messageCount == 0) {
            messageCount = demoConsumer.getMessageCount();
        }

        Assertions.assertTrue(messageCount == 1);
    }

    @AfterAll
    static void afterAll() {
        LocalStack.close();
    }

    @Override
    @NonNull
    public Map<String, String> getProperties() {
        return LocalStack.getProperties();
    }
}
