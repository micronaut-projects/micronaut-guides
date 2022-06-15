package example.micronaut.controllers;

import example.micronaut.models.MessageForm;
import example.micronaut.models.RoomMessage;
import example.micronaut.repositories.RoomRepository;
import io.micronaut.core.annotation.NonNull;
import example.micronaut.services.MessageService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@ExecuteOn(TaskExecutors.IO) // <1>
@Controller("/rooms") // <2>
class MessagesController extends ApplicationController {
    private static final Logger LOG = LoggerFactory.getLogger(MessagesController.class);
    private final MessageService messageService;
    private final RoomRepository roomRepository;

    public MessagesController(MessageService messageService,  // <3>
                              RoomRepository roomRepository) {
        this.messageService = messageService;
        this.roomRepository = roomRepository;
    }

    @View("/messages/create") // <4>
    @Produces(MediaType.TEXT_HTML) // <5>
    @Get("/{id}/messages/create") // <6>
    HttpResponse<?> create(@PathVariable Long id) { // <7>
        return modelResponse(id);
    }

    @Produces(MediaType.TEXT_HTML) // <5>
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <8>
    @Post("/{id}/messages") // <9>
    HttpResponse<?> save(@PathVariable Long id, // <7>
                         @Body("content") String content) { // <10>
        Optional<RoomMessage> roomMessageOptional = messageService.save(new MessageForm(id, content));
        return roomMessageOptional.map(roomMessage -> {
            return redirectTo("/rooms", id);
        }).orElseGet(HttpResponse::notFound);
    }

    private HttpResponse<?> modelResponse(@NonNull Long id) {
        return model(id).map(HttpResponse::ok).orElseGet(HttpResponse::notFound);
    }

    private Optional<Map<String, Object>> model(@NonNull Long id) {
        return roomRepository.findById(id)
                .map(room -> Collections.singletonMap(RoomsController.ROOM, room));
    }
}
