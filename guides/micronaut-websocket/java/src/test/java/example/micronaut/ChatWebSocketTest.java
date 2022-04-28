package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.retry.annotation.Retryable;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.websocket.WebSocketClient;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@MicronautTest
public class ChatWebSocketTest {

    @Inject
    ApplicationContext ctx;

    @Inject
    EmbeddedServer server;

    TestWebSocketClient client1;
    TestWebSocketClient client2;
    TestWebSocketClient client3;

    @BeforeAll
    public void setup() {
        client1 = createWebSocketClient(server.getPort(), "topic_1", "adam");
        client2 = createWebSocketClient(server.getPort(), "topic_1", "anna");
        client3 = createWebSocketClient(server.getPort(), "topic_2", "ben");
    }

    @Test
    @Retryable
    public void should_broadcast_message_when_users_connect_to_specific_topic() {
        expect:
        Assertions.assertEquals(client1.getMessagesChronologically(), Arrays.asList("[adam] Joined!", "[anna] Joined!"));
        Assertions.assertEquals(client2.getMessagesChronologically(), Arrays.asList("[adam] Joined!", "[anna] Joined!"));
        Assertions.assertEquals(client3.getMessagesChronologically(), Arrays.asList("[ben] Joined!"));
    }

    @Test
    @Retryable
    public void should_broadcast_message_to_all_users_inside_the_topic() {
        when:
        client1.send("Hello, everyone!");

        then:
        Assertions.assertEquals(client1.getLatestMessage(), "[adam] Hello everyone!");
        Assertions.assertEquals(client2.getLatestMessage(), "[adam] Hello everyone!");
        Assertions.assertNotEquals(client3.getLatestMessage(), "[adam] Hello everyone!");
    }

    @Test
    @Retryable
    public void should_broadcast_message_when_user_disconnects_from_the_chat() throws Exception {
        when:
        client2.close();

        then:
        Assertions.assertEquals(client1.getLatestMessage(), "[anna] Disconnected!");
    }

    private TestWebSocketClient createWebSocketClient(int port, String topic, String username) {
        WebSocketClient webSocketClient = ctx.getBean(WebSocketClient.class);

        Publisher<TestWebSocketClient> client = webSocketClient.connect(TestWebSocketClient.class, "ws://localhost:${port}/ws/chat/${topic}/${username}");

        return Flowable.fromPublisher(client).blockingFirst();
    }

    @ClientWebSocket
    public static abstract class TestWebSocketClient implements AutoCloseable {

        private final ConcurrentLinkedDeque<String> replies = new ConcurrentLinkedDeque<>();

        public String getLatestMessage() {
            return replies.peekLast();
        }

        public List<String> getMessagesChronologically() {
            return new ArrayList<>(replies);
        }

        @OnOpen
        public void onOpen() {
        }

        @OnMessage
        public void onMessage(String message) {
            replies.add(message);
        }

        @OnClose
        public void onClose() {
        }

        public abstract void send(String message);
    }
}
