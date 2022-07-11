package example.micronaut;

import example.micronaut.entities.Room;
import example.micronaut.models.MessageForm;
import example.micronaut.repositories.MessageRepository;
import example.micronaut.repositories.RoomRepository;
import example.micronaut.services.MessageService;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.websocket.WebSocketClient;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
class ChatServerWebSocketTest {

    @Inject
    RoomRepository roomRepository;

    @Inject
    MessageService messageService;

    @Inject
    @Client("/")
    WebSocketClient webSocketClient;

    @Test
    void testWebsocketTurboStream() {
        Room roomA = roomRepository.save("Room Blue");
        String uri = UriBuilder.of("/chat").path("" + roomA.getId()).build().toString();
        ChatClientWebSocket chatClient = Flux.from(webSocketClient.connect(ChatClientWebSocket.class, uri)).blockFirst();

        messageService.save(new MessageForm(roomA.getId(), "Hola"));

        boolean messageReceived = false;
        await().atMost(5, TimeUnit.SECONDS)
                .until(() -> CollectionUtils.isNotEmpty(chatClient.getReplies()));

        Optional<String> str = chatClient.getReplies().stream().findFirst();
        assertTrue(str.isPresent());
        String turboStream = str.get();
        assertTrue(turboStream.startsWith("<turbo-stream action=\"append\" target=\"messages\"><template><p id=\"message_"));
        assertTrue(turboStream.endsWith(": Hola\n" +
                "</p>\n" +
                "</template></turbo-stream>"));

        roomRepository.deleteById(roomA.getId());
    }

}