package example.micronaut.controllers;

import example.micronaut.repositories.RoomRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.net.URI;
import java.net.URISyntaxException;

@Controller("/rooms") // <1>
public class RoomsControllerDelete extends RoomsController {

    public RoomsControllerDelete(RoomRepository roomRepository) { // <2>
        super(roomRepository);
    }

    @ExecuteOn(TaskExecutors.IO) // <3>
    @Produces(MediaType.TEXT_HTML) // <4>
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <5>
    @Post("/{id}/delete") // <6>
    HttpResponse<?> delete(@PathVariable Long id) throws URISyntaxException { // <7>
        roomRepository.deleteById(id);
        return HttpResponse.seeOther(new URI("/rooms"));
    }
}
