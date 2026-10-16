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

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.ollama.OllamaChatModel;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Singleton
class ChatRequestRecorder implements BeanCreatedEventListener<OllamaChatModel.OllamaChatModelBuilder>, // <1>
        ChatModelListener { // <2>

    private final List<List<ChatMessage>> requests = new CopyOnWriteArrayList<>();

    @Override
    public OllamaChatModel.OllamaChatModelBuilder onCreated(BeanCreatedEvent<OllamaChatModel.OllamaChatModelBuilder> event) {
        return event.getBean()
                .temperature(0.0) // <3>
                .listeners(List.of(this));
    }

    @Override
    public void onRequest(ChatModelRequestContext requestContext) {
        requests.add(requestContext.chatRequest().messages()); // <4>
    }

    List<ChatMessage> lastRequest() {
        return requests.getLast();
    }
}
