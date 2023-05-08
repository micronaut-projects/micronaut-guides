package example.micronaut.models;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public class MessageForm {

    @NonNull
    @NotNull
    private final Long room;

    @NonNull
    @NotBlank
    private final String content;

    public MessageForm(@NonNull Long room, @NonNull String content) {
        this.room = room;
        this.content = content;
    }

    @NonNull
    public Long getRoom() {
        return room;
    }

    @NonNull
    public String getContent() {
        return content;
    }
}
