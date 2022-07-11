package example.micronaut.controllers;

import example.micronaut.models.MessageForm;
import example.micronaut.models.RoomMessage;
import example.micronaut.services.MessageService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ExecuteOn(TaskExecutors.IO) // <1>
@Controller("/rooms") // <2>
class MessagesControllerSave extends ApplicationController {
    private static final Logger LOG = LoggerFactory.getLogger(MessagesControllerSave.class);
    private final MessageService messageService;

    public MessagesControllerSave(MessageService messageService) {  // <3>
        this.messageService = messageService;
    }

    @Produces(MediaType.TEXT_HTML) // <4>
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <5>
    @Post("/{id}/messages") // <6>
    HttpResponse<?> save(@PathVariable Long id, // <7>
                         @Body("content") String content) { // <8>
        Optional<RoomMessage> roomMessageOptional = messageService.save(new MessageForm(id, content));
        return roomMessageOptional.map(roomMessage -> redirectTo("/rooms", id))
                .orElseGet(HttpResponse::notFound);
    }
}
