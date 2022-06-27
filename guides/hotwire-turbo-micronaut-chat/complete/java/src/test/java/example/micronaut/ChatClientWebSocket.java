package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

@ClientWebSocket("/chat/{room}")
public abstract class ChatClientWebSocket implements AutoCloseable {

    private WebSocketSession session;
    private HttpRequest<?> request;
    private String room;
    private Collection<String> replies = new ConcurrentLinkedQueue<>();

    @OnOpen
    public void onOpen(String room,
                       WebSocketSession session,
                       HttpRequest<?> request) {
        this.room = room;
        this.session = session;
        this.request = request;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public HttpRequest<?> getRequest() {
        return request;
    }

    public String getRoom() {
        return room;
    }

    @OnMessage
    public void onMessage(String message) {
        replies.add(message);
    }

    public Collection<String> getReplies() {
        return replies;
    }
}
