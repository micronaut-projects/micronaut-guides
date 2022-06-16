package example.micronaut;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.Writable;
import example.micronaut.models.RoomMessage;
import io.micronaut.views.turbo.TurboStream;
import io.micronaut.views.turbo.TurboStreamAction;
import io.micronaut.views.turbo.TurboStreamRenderer;
import io.micronaut.views.turbo.http.TurboMediaType;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@ServerWebSocket("/chat/{room}") // <1>
public class ChatServerWebSocket implements ApplicationEventListener<RoomMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(ChatServerWebSocket.class);
    private final WebSocketBroadcaster broadcaster;
    private final TurboStreamRenderer turboStreamRenderer;

    private Map<String, Set<String>> roomSessions = new ConcurrentHashMap<>();

    ChatServerWebSocket(WebSocketBroadcaster broadcaster, // <2>
                        TurboStreamRenderer turboStreamRenderer) {
        this.broadcaster = broadcaster;
        this.turboStreamRenderer = turboStreamRenderer;
    }

    @OnOpen // <3>
    public void onOpen(String room, WebSocketSession session) {
        LOG.info("onOpen room {}", room);
        roomSessions.computeIfAbsent(room, k -> {
            Set<String> result = new HashSet<>();
            result.add(session.getId());
            return result;
        });
        roomSessions.computeIfPresent(room, (k, sessions) -> {
            sessions.add(session.getId());
            return sessions;
        });
    }

    @OnMessage // <4>
    public void onMessage(String room, String message, WebSocketSession session) {
        LOG.info("onMessage room {}", room);
    }

    @OnClose // <5>
    public void onClose(String room, WebSocketSession session) {
        LOG.info("onClose room {}", room);
        roomSessions.computeIfPresent(room, (k, sessions) -> {
            sessions.remove(session.getId());
            return sessions;
        });
    }

    @Override
    public void onApplicationEvent(RoomMessage event) {
        broadcast(event);
    }

    private void broadcast(@NonNull RoomMessage message) {
        turboStreamRenderer.render(turboStream(message), null)
                .ifPresent(writable -> broadcast(writable, String.valueOf(message.getRoom())));
    }

    private void broadcast(@NonNull Writable writable, @NonNull String room) {
        writableToString(writable)
                .ifPresent(message -> broadcaster.broadcastAsync(message, TurboMediaType.TURBO_STREAM_TYPE, inRoom(room))); // <2>
    }

    private Predicate<WebSocketSession> inRoom(String room) {
        Set<String> websocketIds = roomSessions.getOrDefault(room, Collections.emptySet());
        return s -> websocketIds.contains(s.getId());
    }

    private Optional<String> writableToString(Writable writable) {
        try {
            StringWriter stringWriter = new StringWriter();
            writable.writeTo(stringWriter);
            return Optional.of(stringWriter.toString());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private TurboStream.Builder turboStream(@NonNull RoomMessage message) {
        return TurboStream.builder()
                .action(TurboStreamAction.APPEND)
                .template("/messages/_message.html", Collections.singletonMap("message", message))
                .targetDomId("messages"); // <6>
    }
}