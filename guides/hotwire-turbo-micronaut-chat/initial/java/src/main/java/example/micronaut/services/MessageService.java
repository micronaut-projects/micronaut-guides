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

import example.micronaut.models.MessageForm;
import example.micronaut.models.RoomMessage;
import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.core.annotation.NonNull;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
@DefaultImplementation(DefaultMessageService.class) // <1>
public interface MessageService {
    @NonNull
    Optional<RoomMessage> save(@NonNull @NotNull @Valid MessageForm form); // <2>
}
