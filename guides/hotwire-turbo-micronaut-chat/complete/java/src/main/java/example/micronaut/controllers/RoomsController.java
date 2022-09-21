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
