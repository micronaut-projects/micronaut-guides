package example.micronaut.controllers;

import example.micronaut.repositories.RoomRepository;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.views.View;

import java.util.Collections;
import java.util.Map;

@Controller("/rooms") // <1>
public class RoomsControllerCreate extends RoomsController {

    public RoomsControllerCreate(RoomRepository roomRepository) { // <2>
        super(roomRepository);
    }

    @View("/rooms/create") // <3>
    @Get("/create") // <4>
    @Produces(MediaType.TEXT_HTML) // <5>
    Map<String, Object> create() {
        return Collections.emptyMap();
    }
}
