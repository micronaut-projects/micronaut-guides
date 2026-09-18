package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

class QuotersClientTest {

    @Test
    void onStartupQuotersServerIsInvoked() {
        EmbeddedServer quotersServer = ApplicationContext.run(EmbeddedServer.class, Map.of("micronaut.server.port", "-1", "spec.name", "quoters"));
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class,
                Map.of("micronaut.server.port", "-1",
                        "micronaut.http.services.quoters.url", "http://localhost:" + quotersServer.getPort()));

        Quote expected = new Quote("success", new Value(2L, "You can configure your Micronaut application with properties, TOML, YAML, Groovy or Config4k"));

        await().atMost(5, SECONDS).until(() -> 1 == quotersServer.getApplicationContext().getBean(QuotersController.class).sentQuotes.size() &&
                expected.equals(quotersServer.getApplicationContext().getBean(QuotersController.class).sentQuotes.get(0)));
        server.close();
        quotersServer.close();
    }

    @Requires(property = "spec.name", value = "quoters")
    @Controller
    static class QuotersController {
        public List<Quote> sentQuotes = new ArrayList<>();

        @Get("/api/random")
        Quote random() {
            Quote quote = new Quote("success", new Value(2L, "You can configure your Micronaut application with properties, TOML, YAML, Groovy or Config4k"));
            sentQuotes.add(quote);
            return quote;
        }

    }
}
