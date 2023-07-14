package example.micronaut.services;

import example.micronaut.entities.Message;
import example.micronaut.models.MessageForm;
import example.micronaut.models.RoomMessage;
import example.micronaut.repositories.MessageRepository;
import example.micronaut.repositories.RoomRepository;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;

@Singleton // <1>
public class DefaultMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final ApplicationEventPublisher<RoomMessage> eventPublisher;

    public DefaultMessageService(MessageRepository messageRepository,
                                 RoomRepository roomRepository,
                                 ApplicationEventPublisher<RoomMessage> eventPublisher) {  // <2>
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @NonNull
    public Optional<RoomMessage> save(@NonNull @NotNull @Valid MessageForm form) {
        return roomRepository.findById(form.getRoom())
                .map(room -> {
                    Message message = messageRepository.save(new Message(form.getContent(), room));
                    RoomMessage roomMessage = new RoomMessage(message.getId(),
                            form.getRoom(),
                            form.getContent(),
                            message.getDateCreated());
                    eventPublisher.publishEvent(roomMessage); // <2>
                    return roomMessage;
                });
    }
}
