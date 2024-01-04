/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut.models;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Serdeable // <1>
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
