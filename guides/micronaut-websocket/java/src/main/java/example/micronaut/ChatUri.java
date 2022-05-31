package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.beans.BeanMap;
import io.micronaut.http.uri.UriBuilder;

import javax.validation.constraints.NotBlank;
import java.net.URI;

@Introspected // <1>
public class ChatUri {

    private final String username; // <2>
    private final String topic; // <3>

    private transient final UriBuilder uriBuilder;

    public ChatUri() {
        this(0, null, null);
    }

    public ChatUri(int port, @NotBlank String username, @NotBlank String topic) {
        this.uriBuilder
                = UriBuilder.of("ws://localhost").port(port).path("/ws/chat/{topic}/{username}");
        this.username = username;
        this.topic = topic;
    }

    public URI delegate() {
        return uriBuilder.expand(BeanMap.of(this));
    }

    public String getUsername() {
        return username;
    }

    public String getTopic() {
        return topic;
    }
}
