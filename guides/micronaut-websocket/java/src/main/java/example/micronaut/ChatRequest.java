package example.micronaut;

import io.micronaut.core.annotation.Introspected;

@Introspected // <1>
public class ChatRequest {

    private final String username; // <2>
    private final String topic; // <3>
    private final String message; // <4>

    public ChatRequest() {
        this(null, null, null);
    }

    public ChatRequest(String username, String topic) {
        this(username, topic, null);
    }

    public ChatRequest(String username, String topic, String message) {
        this.username = username;
        this.topic = topic;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }
}
