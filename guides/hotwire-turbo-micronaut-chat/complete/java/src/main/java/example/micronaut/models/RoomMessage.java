package example.micronaut.models;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Serdeable
public class RoomMessage {

    private static final String PATTERN = "MMM:dd HH:mm:ss";

    @NonNull
    @NotNull
    private final Long id;

    @NonNull
    @NotNull
    private final Long room;

    @NonNull
    @NotBlank
    private final String content;

    @Nullable
    private final Instant dateCreated;

    public RoomMessage(@NonNull Long id,
                       @NonNull Long room,
                       @NonNull String content,
                       @Nullable Instant dateCreated) {
        this.id = id;
        this.room = room;
        this.content = content;
        this.dateCreated = dateCreated;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    @NonNull
    public Long getRoom() {
        return room;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    @Nullable
    public Instant getDateCreated() {
        return dateCreated;
    }

    public String formattedDateCreated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN)
                .withZone(ZoneId.systemDefault());
        return formatter.format(dateCreated);
    }


}
