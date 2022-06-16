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
public class RoomsControllerEdit extends RoomsController {

    public RoomsControllerEdit(RoomRepository roomRepository) { // <2>
        super(roomRepository);
    }

    @ExecuteOn(TaskExecutors.IO) // <3>
    @View("/rooms/edit") // <4>
    @Get("/{id}/edit") // <5>
    @Produces(MediaType.TEXT_HTML) // <6>
    HttpResponse<?> edit(@PathVariable Long id) { // <7>
        return modelResponse(id);
    }
}
