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

@Controller("/rooms") // <1>
public class RoomsController extends ApplicationController {

    public static final String ROOM = "room";
    public static final String ROOMS = "rooms";
    private final RoomRepository roomRepository;

    public RoomsController(RoomRepository roomRepository) { // <2>
        this.roomRepository = roomRepository;
    }

    @ExecuteOn(TaskExecutors.IO) // <3>
    @View("/rooms/index") // <4>
    @Get // <5>
    @Produces(MediaType.TEXT_HTML) // <6>
    Map<String, Object> index() {
        return Collections.singletonMap(ROOMS, roomRepository.findAll());
    }

    @View("/rooms/create") // <4>
    @Get("/create") // <7>
    @Produces(MediaType.TEXT_HTML) // <6>
    Map<String, Object> create() {
        return Collections.emptyMap();
    }

    @ExecuteOn(TaskExecutors.IO) // <3>
    @Produces(MediaType.TEXT_HTML) // <6>
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <7>
    @Post // <8>
    HttpResponse<?> save(@Body("name") String name) { // <9>
        return redirectTo("/rooms", roomRepository.save(name).getId());
    }

    @ExecuteOn(TaskExecutors.IO) // <3>
    @View("/rooms/edit") // <4>
    @Get("/{id}/edit") // <5>
    @Produces(MediaType.TEXT_HTML) // <6>
    HttpResponse<?> edit(@PathVariable Long id) { // <10>
        return modelResponse(id);
    }

    @ExecuteOn(TaskExecutors.IO) // <3>
    @Produces(MediaType.TEXT_HTML) // <4>
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <7>
    @Post("/update") // <8>
    HttpResponse<?> update(@Body("id") Long id,  // <9>
                           @Body("name") String name) { // <9>
        roomRepository.update(id, name);
        return redirectTo("/rooms", id);
    }

    @ExecuteOn(TaskExecutors.IO) // <3>
    @Produces(MediaType.TEXT_HTML) // <4>
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <7>
    @Post("/{id}/delete") // <8>
    HttpResponse<?> delete(@PathVariable Long id) throws URISyntaxException { // <10>
        roomRepository.deleteById(id);
        return HttpResponse.seeOther(new URI("/rooms"));
    }

    @ExecuteOn(TaskExecutors.IO) // <3>
    @View("/rooms/show") // <4>
    @Get("/{id}") // <5>
    @Produces(MediaType.TEXT_HTML) // <4>
    HttpResponse<?> show(@PathVariable Long id) { // <10>
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
