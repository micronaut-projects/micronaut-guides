package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@ClientWebSocket // <1>
public abstract class ChatClientEndpoint implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(ChatClientEndpoint.class);

    private final Deque<String> messageHistory = new ConcurrentLinkedDeque<>(); // <2>

    public String getLatestMessage() {
        return messageHistory.peekLast();
    }

    public List<String> getMessagesChronologically() {
        return new ArrayList<>(messageHistory);
    }

    @OnOpen // <3>
    public void onOpen() {
    }

    @OnMessage // <4>
    public void onMessage(String message) {
        log.info("* Storing message in client: {}", message);
        messageHistory.add(message); // <5>
    }

    @OnClose // <6>
    public void onClose() {
    }

    public abstract void broadcastChat(@NonNull String message); // <7>
}