package example.micronaut;

import io.micronaut.core.annotation.Introspected;

@Introspected // <1>
public class ChatRequest {

    private final String username; // <2>
    private final String topic; // <3>

    public ChatRequest() {
        this(null, null);
    }

    public ChatRequest(String username, String topic) {
        this.username = username;
        this.topic = topic;
    }

    public String getUsername() {
        return username;
    }

    public String getTopic() {
        return topic;
    }
}
