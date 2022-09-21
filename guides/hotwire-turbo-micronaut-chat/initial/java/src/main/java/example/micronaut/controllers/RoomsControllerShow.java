package example.micronaut.controllers;

import example.micronaut.repositories.RoomRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.View;

@Controller("/rooms") // <1>
public class RoomsControllerShow extends RoomsController {

    public RoomsControllerShow(RoomRepository roomRepository) { // <2>
        super(roomRepository);
    }

    @ExecuteOn(TaskExecutors.IO) // <3>
    @View("/rooms/show") // <4>
    @Get("/{id}") // <5>
    @Produces(MediaType.TEXT_HTML) // <6>
    HttpResponse<?> show(@PathVariable Long id) { // <7>
        return modelResponse(roomRepository.getById(id).orElse(null));
    }
}
