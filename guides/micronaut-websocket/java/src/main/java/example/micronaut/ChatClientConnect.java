package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.websocket.WebSocketClient;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@Singleton // <1>
public class ChatClientConnect {
    private final ApplicationContext appContext;

    public ChatClientConnect(ApplicationContext appContext) { // <2>
        this.appContext = appContext;
    }

    public ChatClientEndpoint connect(@NonNull ChatUri uri) { // <3>
        WebSocketClient webSocketClient = appContext.getBean(WebSocketClient.class);
        Publisher<ChatClientEndpoint> client = webSocketClient.connect(ChatClientEndpoint.class, uri.delegate());

        return Flux.from(client).blockFirst();
    }
}
