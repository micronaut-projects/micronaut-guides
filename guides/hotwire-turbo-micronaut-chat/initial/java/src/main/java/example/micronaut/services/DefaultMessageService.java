/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
