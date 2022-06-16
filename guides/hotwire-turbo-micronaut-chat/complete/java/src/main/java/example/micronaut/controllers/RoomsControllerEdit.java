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
import io.micronaut.views.turbo.TurboFrameView;

@Controller("/rooms")
public class RoomsControllerEdit extends RoomsController {

    public RoomsControllerEdit(RoomRepository roomRepository) {
        super(roomRepository);
    }

    @ExecuteOn(TaskExecutors.IO)
    @View("/rooms/edit")
    @Get("/{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    @TurboFrameView("/rooms/_edit") // <1>
    HttpResponse<?> edit(@PathVariable Long id) {
        return modelResponse(id);
    }
}
