package example.micronaut.controllers;

import example.micronaut.repositories.RoomRepository;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.View;

import java.util.Collections;
import java.util.Map;

@Controller("/rooms") // <1>
public class RoomsControllerIndex extends RoomsController {
    public static final String ROOMS = "rooms";

    public RoomsControllerIndex(RoomRepository roomRepository) { // <2>
        super(roomRepository);
    }

    @ExecuteOn(TaskExecutors.IO) // <3>
    @View("/rooms/index") // <4>
    @Get // <5>
    @Produces(MediaType.TEXT_HTML) // <6>
    Map<String, Object> index() {
        return Collections.singletonMap(ROOMS, roomRepository.findAll());
    }
}
