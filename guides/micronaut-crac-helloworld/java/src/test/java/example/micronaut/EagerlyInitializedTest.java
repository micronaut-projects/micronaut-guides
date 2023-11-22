package example.micronaut;

import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EagerlyInitializedTest {
    @Test
    void singletonsAreEagerlyInitialized() throws InterruptedException {
        ApplicationContext ctx = ApplicationContext.run();
        sleep(5_000);
        assertTrue(ctx.getBean(Clock.class).getNow().isBefore(LocalDateTime.now().minusSeconds(3)));
        ctx.close();
    }

}