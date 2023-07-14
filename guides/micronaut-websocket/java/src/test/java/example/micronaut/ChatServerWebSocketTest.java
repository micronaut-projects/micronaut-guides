package example.micronaut;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.websocket.WebSocketClient;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnMessage;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Property(name = "spec.name", value = "ChatWebSocketTest") // <1>
@MicronautTest // <2>
class ChatServerWebSocketTest {

    @Inject
    BeanContext beanContext;

    @Inject
    EmbeddedServer embeddedServer;

    @Requires(property = "spec.name", value = "ChatWebSocketTest") // <1>
    @ClientWebSocket // <3>
    static abstract class TestWebSocketClient implements AutoCloseable { // <4>

        private final Deque<String> messageHistory = new ConcurrentLinkedDeque<>();

        public String getLatestMessage() {
            return messageHistory.peekLast();
        }

        public List<String> getMessagesChronologically() {
            return new ArrayList<>(messageHistory);
        }

        @OnMessage  // <5>
        void onMessage(String message) {
            messageHistory.add(message);
        }

        abstract void send(@NonNull @NotBlank String message); // <6>
    }

    private TestWebSocketClient createWebSocketClient(int port, String username, String topic) {
        WebSocketClient webSocketClient = beanContext.getBean(WebSocketClient.class);
        URI uri = UriBuilder.of("ws://localhost")
                .port(port)
                .path("ws")
                .path("chat")
                .path("{topic}")
                .path("{username}")
                .expand(CollectionUtils.mapOf("topic", topic, "username", username));
        Publisher<TestWebSocketClient> client = webSocketClient.connect(TestWebSocketClient.class,  uri); // <7>
        return Flux.from(client).blockFirst();
    }

    @Test
    void testWebsockerServer() throws Exception {
        TestWebSocketClient adam = createWebSocketClient(embeddedServer.getPort(), "adam", "Cats & Recreation"); // <8>
        await().until(() -> // <9>
                Collections.singletonList("[adam] Joined Cats & Recreation!")
                        .equals(adam.getMessagesChronologically()));

        TestWebSocketClient anna = createWebSocketClient(embeddedServer.getPort(), "anna", "Cats & Recreation");
        await().until(() -> // <9>
                Collections.singletonList("[anna] Joined Cats & Recreation!")
                        .equals(anna.getMessagesChronologically()));
        await().until(() -> // <9>
                Arrays.asList("[adam] Joined Cats & Recreation!", "[anna] Joined Cats & Recreation!")
                        .equals(adam.getMessagesChronologically()));

        TestWebSocketClient ben = createWebSocketClient(embeddedServer.getPort(), "ben", "Fortran Tips & Tricks");
        await().until(() -> // <9>
                Collections.singletonList("[ben] Joined Fortran Tips & Tricks!")
                        .equals(ben.getMessagesChronologically()));
        TestWebSocketClient zach = createWebSocketClient(embeddedServer.getPort(), "zach", "all");
        await().until(() -> // <9>
                Collections.singletonList("[zach] Now making announcements!")
                        .equals(zach.getMessagesChronologically()));
        TestWebSocketClient cienna = createWebSocketClient(embeddedServer.getPort(), "cienna", "Fortran Tips & Tricks");
        await().until(() -> // <9>
                Collections.singletonList("[cienna] Joined Fortran Tips & Tricks!")
                        .equals(cienna.getMessagesChronologically()));
        await().until(() -> // <9>
                Arrays.asList("[ben] Joined Fortran Tips & Tricks!", "[zach] Now making announcements!", "[cienna] Joined Fortran Tips & Tricks!") // <10>
                        .equals(ben.getMessagesChronologically()));

        // should broadcast message to all users inside the topic // <11>
        final String adamsGreeting = "Hello, everyone. It's another purrrfect day :-)";
        final String expectedGreeting = "[adam] " + adamsGreeting;
        adam.send(adamsGreeting);

        //subscribed to "Cats & Recreation"
        await().until(() ->  // <9>
                expectedGreeting.equals(adam.getLatestMessage()));

        //subscribed to "Cats & Recreation"
        await().until(() ->  // <9>
                expectedGreeting.equals(anna.getLatestMessage()));

        //NOT subscribed to "Cats & Recreation"
        assertNotEquals(expectedGreeting, ben.getLatestMessage());

        //subscribed to the special "all" topic
        await().until(() ->  // <9>
                expectedGreeting.equals(zach.getLatestMessage()));

        //NOT subscribed to "Cats & Recreation"
        assertNotEquals(expectedGreeting, cienna.getLatestMessage());

        // should broadcast message when user disconnects from the chat // <12>

        anna.close();

        String annaLeaving = "[anna] Leaving Cats & Recreation!";
        await().until(() ->  // <9>
                annaLeaving.equals(adam.getLatestMessage()));

        //subscribed to "Cats & Recreation"
        assertEquals(annaLeaving, adam.getLatestMessage());

        //Anna already left and therefore won't see the message about her leaving
        assertNotEquals(annaLeaving, anna.getLatestMessage());

        //NOT subscribed to "Cats & Recreation"
        assertNotEquals(annaLeaving, ben.getLatestMessage());

        //subscribed to the special "all" topic
        assertEquals(annaLeaving, zach.getLatestMessage());

        //NOT subscribed to "Cats & Recreation"
        assertNotEquals(annaLeaving, cienna.getLatestMessage());

        adam.close();
        ben.close();
        zach.close();
        cienna.close();
    }
}
