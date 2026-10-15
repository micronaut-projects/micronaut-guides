/*
 * Copyright 2017-2026 original authors
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
package example.micronaut;

import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ChatRepository {

    // <1>
    private final List<ChatMessage> messages = new ArrayList<>();
    // <2>
    private final Sinks.Many<ChatMessage> sink = Sinks.many().multicast().onBackpressureBuffer();

    public List<ChatMessage> findAll() {
        return List.copyOf(messages);
    }

    public ChatMessage save(String from, String text) {
        ChatMessage message = new ChatMessage(from, text);
        messages.add(message);
        sink.tryEmitNext(message);
        return message;
    }

    public Flux<ChatMessage> stream() {
        return sink.asFlux();
    }
}
