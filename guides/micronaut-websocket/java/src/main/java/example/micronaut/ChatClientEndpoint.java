package example.micronaut;

import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@ClientWebSocket // <1>
public abstract class ChatClientEndpoint implements AutoCloseable {

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
        System.out.println("* Storing message in client: " + message);
        messageHistory.add(message); // <5>
    }

    @OnClose // <6>
    public void onClose() {
    }

    public abstract void broadcastChat(String message); // <7>
}