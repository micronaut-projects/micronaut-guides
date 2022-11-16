package example.micronaut

import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest
class MicronautguideTest implements TestPropertyProvider {

    @Inject
    DemoController demoController

    @Inject
    DemoConsumer demoConsumer

    @Test
    void testItWorks() {
        int messageCount = demoConsumer.getMessageCount()
        assert messageCount == 0
        demoController.publishDemoMessages()
        messageCount = demoConsumer.getMessageCount()
        while (messageCount == 0) {
            messageCount = demoConsumer.getMessageCount()
        }
        assert messageCount == 1
    }

    @AfterAll
    static void afterAll() {
        LocalStack.close()
    }

    @Override
    @NonNull
    Map<String, String> getProperties() {
        LocalStack.getProperties()
    }

}
