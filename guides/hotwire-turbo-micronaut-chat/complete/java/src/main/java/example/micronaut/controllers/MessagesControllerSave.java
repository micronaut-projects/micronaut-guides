package example.micronaut.controllers;

import example.micronaut.models.MessageForm;
import example.micronaut.models.RoomMessage;
import example.micronaut.services.MessageService;
import io.micronaut.http.HttpRequest;
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
import io.micronaut.views.turbo.TurboStream;
import io.micronaut.views.turbo.http.TurboMediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Optional;

@ExecuteOn(TaskExecutors.IO)
@Controller("/rooms")
class MessagesControllerSave extends ApplicationController {
    private static final Logger LOG = LoggerFactory.getLogger(MessagesControllerSave.class);
    private final MessageService messageService;

    public MessagesControllerSave(MessageService messageService) {
        this.messageService = messageService;
    }

    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/{id}/messages")
    HttpResponse<?> save(@PathVariable Long id,
                         @Body("content") String content,
                         HttpRequest<?> request) {
        Optional<RoomMessage> roomMessageOptional = messageService.save(new MessageForm(id, content));
        return roomMessageOptional.map(roomMessage -> {
            if (TurboMediaType.acceptsTurboStream(request)) { // <1>
                return HttpResponse.ok(TurboStream.builder() // <2>
                        .template("/messages/_message.html", Collections.singletonMap("message", roomMessage))
                        .targetDomId("messages")
                        .append());
            }
            return redirectTo("/rooms", id);
        }).orElseGet(HttpResponse::notFound);
    }
}
