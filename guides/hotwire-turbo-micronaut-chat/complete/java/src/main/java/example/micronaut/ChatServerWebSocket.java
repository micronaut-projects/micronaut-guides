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
import java.util.Collections;
import java.util.Optional;

@ServerWebSocket("/chat/{room}")
public class ChatServerWebSocket implements ApplicationEventListener<RoomMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(ChatServerWebSocket.class);
    private final WebSocketBroadcaster broadcaster;
    private final TurboStreamRenderer turboStreamRenderer;

    ChatServerWebSocket(WebSocketBroadcaster broadcaster,
                        TurboStreamRenderer turboStreamRenderer) {
        this.broadcaster = broadcaster;
        this.turboStreamRenderer = turboStreamRenderer;
    }

    @OnOpen
    public void onOpen(String room, WebSocketSession session) {
        LOG.info("onOpen room {}", room);
    }

    @OnMessage
    public void onMessage(String room, String message, WebSocketSession session) {
        LOG.info("onMessage room {}", room);
    }

    @OnClose
    public void onClose(String room, WebSocketSession session) {
        LOG.info("onClose room {}", room);
    }

    @Override
    public void onApplicationEvent(RoomMessage event) {
        broadcast(event);
    }

    private void broadcast(@NonNull RoomMessage message) {
        turboStreamRenderer.render(turboStream(message), null)
                .ifPresent(this::broadcast);
    }

    private void broadcast(@NonNull Writable writable) {
        writableToString(writable)
                .ifPresent(message -> broadcaster.broadcastAsync(message, TurboMediaType.TURBO_STREAM_TYPE));
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
                .targetDomId("messages");
    }
}