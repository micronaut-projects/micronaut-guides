package example.micronaut.controllers;

import example.micronaut.repositories.RoomRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.View;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@ExecuteOn(TaskExecutors.IO) // <1>
@Controller("/rooms") // <2>
class MessagesControllerCreate extends ApplicationController {
    private final RoomRepository roomRepository;

    public MessagesControllerCreate(RoomRepository roomRepository) { // <3>
        this.roomRepository = roomRepository;
    }

    @View("/messages/create") // <4>
    @Produces(MediaType.TEXT_HTML) // <5>
    @Get("/{id}/messages/create") // <6>
    HttpResponse<?> create(@PathVariable Long id) { // <7>
        return modelResponse(id);
    }

    private HttpResponse<?> modelResponse(@NonNull Long id) {
        return model(id).map(HttpResponse::ok).orElseGet(HttpResponse::notFound);
    }

    private Optional<Map<String, Object>> model(@NonNull Long id) {
        return roomRepository.findById(id)
                .map(room -> Collections.singletonMap(RoomsController.ROOM, room));
    }
}
