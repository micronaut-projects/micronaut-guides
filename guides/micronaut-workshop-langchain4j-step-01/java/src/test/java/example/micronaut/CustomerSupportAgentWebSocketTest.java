package example.micronaut;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.websocket.WebSocketClient;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnMessage;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.awaitility.Awaitility.await;

@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".*")
@MicronautTest
@Property(name = "spec.name", value = "CustomerSupportAgentWebSocketTest")
class CustomerSupportAgentWebSocketTest {
    @Inject
    WebSocketClient webSocketClient;

    @Inject
    EmbeddedServer embeddedServer;

    @Test
    void testWebSocketConnection() {
        WebSocketClient client = WebSocketClient.create(URI.create("ws://localhost:" + embeddedServer.getPort() + "/customer-support-agent"));
        TestWebSocketClient chat = createWebSocketClient(embeddedServer.getPort());
        await().until(() ->
                Collections.singletonList("Welcome to Miles of Smiles! How can I help you today?")
                        .equals(chat.getMessagesChronologically()));
        chat.send("Say Hello");
        await().until(() ->
                chat.getMessagesChronologically().stream().anyMatch(it -> it.contains("Hello")));
        client.close();
    }

    private TestWebSocketClient createWebSocketClient(int port) {
        URI uri = createWebSocketServerUri(port);
        Publisher<TestWebSocketClient> client = webSocketClient.connect(TestWebSocketClient.class,  uri);
        return Flux.from(client).blockFirst();
    }

    private static URI createWebSocketServerUri(int port) {
        return UriBuilder.of("ws://localhost")
                .port(port)
                .path("customer-support-agent")
                .build();
    }

    @Requires(property = "spec.name", value = "CustomerSupportAgentWebSocketTest")
    @ClientWebSocket
    abstract static class TestWebSocketClient implements AutoCloseable {
        private final Deque<String> messageHistory = new ConcurrentLinkedDeque<>();
        public List<String> getMessagesChronologically() {
            return new ArrayList<>(messageHistory);
        }

        @OnMessage
        void onMessage(String message) {
            messageHistory.add(message);
        }

        abstract void send(@NonNull String message);
    }
}