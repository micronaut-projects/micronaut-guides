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
package example.micronaut.controllers;

import example.micronaut.entities.Room;
import example.micronaut.repositories.RoomRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public abstract class RoomsController extends ApplicationController {

    public static final String ROOM = "room";
    protected final RoomRepository roomRepository;

    public RoomsController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @NonNull
    private Optional<Map<String, ?>> model(@Nullable Room room) {
        return room == null ? Optional.empty() : Optional.of(Collections.singletonMap(ROOM, room));
    }

    @NonNull
    private Optional<Map<String, ?>> model(@NonNull Long id) {
        return model(roomRepository.findById(id).orElse(null));
    }

    @NonNull
    protected HttpResponse<?> modelResponse(@NonNull Long id) {
        return model(id).map(HttpResponse::ok).orElseGet(HttpResponse::notFound);
    }

    @NonNull
    protected HttpResponse<?> modelResponse(@Nullable Room room) {
        return model(room).map(HttpResponse::ok).orElseGet(HttpResponse::notFound);
    }
}
