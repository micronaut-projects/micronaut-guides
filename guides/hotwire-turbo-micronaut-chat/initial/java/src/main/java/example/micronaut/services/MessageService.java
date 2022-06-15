package example.micronaut.services;

import example.micronaut.models.MessageForm;
import example.micronaut.models.RoomMessage;
import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.core.annotation.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;
@DefaultImplementation(DefaultMessageService.class) // <1>
public interface MessageService {
    @NonNull
    Optional<RoomMessage> save(@NonNull @NotNull @Valid MessageForm form); // <2>
}
