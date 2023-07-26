package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.awaitility.Durations;
import org.junit.jupiter.api.Test;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class ScheduledTasksTest {
    @Test
    void reportCurrentTime() {
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
        if (logger instanceof ch.qos.logback.classic.Logger logbackLogger) {
            logbackLogger.addAppender(listAppender);
        }
        await().atMost(Durations.TEN_SECONDS).untilAsserted(() -> {
            assertTrue(listAppender.list.size() >= 2);
        });
    }
}