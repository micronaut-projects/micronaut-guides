/*
 * Copyright 2017-2023 original authors
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
package example.micronaut.entities;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@MappedEntity // <1>
public class Message {
    @Id // <2>
    @GeneratedValue(GeneratedValue.Type.AUTO) // <3>
    private Long id;

    @NonNull
    @NotBlank
    private String content;

    @Nullable
    @Relation(value = Relation.Kind.MANY_TO_ONE) // <4>
    private Room room;

    @DateCreated // <5>
    @Nullable
    private Instant dateCreated;

    @Creator // <6>
    public Message() {
    }

    public Message(@NonNull String content, @Nullable Room room) {
        this.content = content;
        this.room = room;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    @Nullable
    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(@Nullable Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Nullable
    public Room getRoom() {
        return room;
    }

    public void setRoom(@Nullable Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }

    private static final String PATTERN = "MMM:dd HH:mm:ss";

    public String formattedDateCreated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN)
                .withZone(ZoneId.systemDefault());
        return formatter.format(dateCreated);
    }
}
