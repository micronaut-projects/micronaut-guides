package example.micronaut;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

@ServerWebSocket("/ws/chat/{topic}/{username}") // <1>
public class ChatServer {

    private static final Logger log = LoggerFactory.getLogger(ChatServer.class);

    private final WebSocketBroadcaster broadcaster;

    public ChatServer(WebSocketBroadcaster broadcaster) { // <2>
        this.broadcaster = broadcaster;
    }

    @OnOpen // <3>
    public Publisher<String> onOpen(String topic, String username, WebSocketSession session) {

        log("onOpen", session, username, topic);
        if (topic.equals("all")) { // <4>
            return broadcaster.broadcast(String.format("[%s] Now making announcements!", username), isValid(topic));
        }
        return broadcaster.broadcast(String.format("[%s] Joined %s!", username, topic), isValid(topic));
    }

    @OnMessage // <5>
    public Publisher<String> onMessage(
            String topic,
            String username,
            String message,
            WebSocketSession session) {

        log("onMessage", session, username, topic);
        return broadcaster.broadcast(String.format("[%s] %s", username, message), isValid(topic));
    }

    @OnClose // <6>
    public Publisher<String> onClose(
            String topic,
            String username,
            WebSocketSession session) {

        log("onClose", session, username, topic);
        return broadcaster.broadcast(String.format("[%s] Leaving %s!", username, topic), isValid(topic));
    }

    private void log(String event, WebSocketSession session, String username, String topic) {
        log.info("* WebSocket: {} received for session {} from '{}' regarding '{}'",
                event, session.getId(), username, topic);
    }

    private Predicate<WebSocketSession> isValid(String topic) { // <7>
        return s -> topic.equals("all") //broadcast to all users
                || "all".equals(s.getUriVariables().get("topic", String.class, null)) //"all" subscribes to every topic
                || topic.equalsIgnoreCase(s.getUriVariables().get("topic", String.class, null)); //intra-topic chat
    }
}
