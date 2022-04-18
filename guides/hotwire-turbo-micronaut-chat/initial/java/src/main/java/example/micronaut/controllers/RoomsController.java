package example.micronaut.controllers;

import example.micronaut.repositories.RoomRepository;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.View;
import example.micronaut.entities.Room;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Controller("/rooms")
public class RoomsController extends ApplicationController {

    public static final String ROOM = "room";
    public static final String ROOMS = "rooms";
    private final RoomRepository roomRepository;

    public RoomsController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @ExecuteOn(TaskExecutors.IO)
    @View("/rooms/index")
    @Get
    @Produces(MediaType.TEXT_HTML)
    Map<String, Object> index() {
        return Collections.singletonMap(ROOMS, roomRepository.findAll());
    }

    @View("/rooms/create")
    @Get("/create")
    @Produces(MediaType.TEXT_HTML)
    Map<String, Object> create() {
        return Collections.emptyMap();
    }

    @ExecuteOn(TaskExecutors.IO)
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post
    HttpResponse<?> save(@Body("name") String name) {
        return redirectTo("/rooms", roomRepository.save(name).getId());
    }

    @ExecuteOn(TaskExecutors.IO)
    @View("/rooms/edit")
    @Get("/{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    HttpResponse<?> edit(@PathVariable Long id) {
        return modelResponse(id);
    }

    @ExecuteOn(TaskExecutors.IO)
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/update")
    HttpResponse<?> update(@Body("id") Long id, @Body("name") String name) {
        roomRepository.update(id, name);
        return redirectTo("/rooms", id);
    }

    @ExecuteOn(TaskExecutors.IO)
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/{id}/delete")
    HttpResponse<?> delete(@PathVariable Long id) throws URISyntaxException {
        roomRepository.deleteById(id);
        return HttpResponse.seeOther(new URI("/rooms"));
    }

    @ExecuteOn(TaskExecutors.IO)
    @View("/rooms/show")
    @Get("/{id}")
    @Produces(MediaType.TEXT_HTML)
    HttpResponse<?> show(@PathVariable Long id) {
        return modelResponse(roomRepository.getById(id).orElse(null));
    }

    @NonNull
    private Optional<Map<String, ?>> model(@Nullable Room room) {
        return room == null ? Optional.empty() : Optional.of(Collections.singletonMap(ROOM, room));
    }

    private Optional<Map<String, ?>> model(@NonNull Long id) {
        return model(roomRepository.findById(id).orElse(null));
    }

    private HttpResponse<?> modelResponse(@NonNull Long id) {
        return model(id).map(HttpResponse::ok).orElseGet(HttpResponse::notFound);
    }

    private HttpResponse<?> modelResponse(@Nullable Room room) {
        return model(room).map(HttpResponse::ok).orElseGet(HttpResponse::notFound);
    }
}
