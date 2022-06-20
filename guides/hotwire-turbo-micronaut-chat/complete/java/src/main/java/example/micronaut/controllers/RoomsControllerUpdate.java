package example.micronaut.controllers;

import example.micronaut.repositories.RoomRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/rooms") // <1>
public class RoomsControllerUpdate extends RoomsController {

    public RoomsControllerUpdate(RoomRepository roomRepository) { // <2>
        super(roomRepository);
    }

    @ExecuteOn(TaskExecutors.IO) // <3>
    @Produces(MediaType.TEXT_HTML) // <4>
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <5>
    @Post("/update") // <6>
    HttpResponse<?> update(@Body("id") Long id,  // <7>
                           @Body("name") String name) { // <7>
        roomRepository.update(id, name);
        return redirectTo("/rooms", id);
    }
}
