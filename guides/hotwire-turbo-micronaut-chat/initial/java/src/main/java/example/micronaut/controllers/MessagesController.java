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

@ExecuteOn(TaskExecutors.IO)
@Controller("/rooms")
class MessagesController extends ApplicationController {
    private static final Logger LOG = LoggerFactory.getLogger(MessagesController.class);
    private final MessageService messageService;
    private final RoomRepository roomRepository;

    public MessagesController(MessageService messageService,
                              RoomRepository roomRepository) {
        this.messageService = messageService;
        this.roomRepository = roomRepository;
    }

    @View("/messages/create")
    @Produces(MediaType.TEXT_HTML)
    @Get("/{id}/messages/create")
    HttpResponse<?> create(@PathVariable Long id) {
        return modelResponse(id);
    }

    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/{id}/messages")
    @Status(HttpStatus.OK)
    HttpResponse<?> save(@PathVariable Long id,
                         @Body("content") String content) {
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
