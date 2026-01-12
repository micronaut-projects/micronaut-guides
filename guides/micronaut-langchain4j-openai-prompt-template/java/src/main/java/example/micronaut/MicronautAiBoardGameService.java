/*
 * Copyright 2017-2025 original authors
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

import dev.langchain4j.model.chat.ChatModel;
import io.micronaut.context.exceptions.ConfigurationException;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;

@Singleton // <1>
public class MicronautAiBoardGameService implements BoardGameService {
    private static final String PROMPT_TEMPLATE_PATH = "classpath:promptTemplates/questionPromptTemplate.st";
    private final ChatModel chatModel;
    private final PromptLoader promptLoader;


    public MicronautAiBoardGameService(ChatModel chatModel,
                                       PromptLoader promptLoader) { // <2>
        this.chatModel = chatModel;
        this.promptLoader = promptLoader;
    }

    @Override
    public Answer askQuestion(Question question) {
        String userMessage = userMessage(question);
        return new Answer(question.gameTitle(), chatModel.chat(userMessage));
    }

    public @NonNull String userMessage(@NonNull Question question) {
        return promptLoader.prompt(PROMPT_TEMPLATE_PATH,
                        template -> template.add("game", question.gameTitle()).add("question", question.question()))
                .orElseThrow(() -> new ConfigurationException("Prompt Template not found " + PROMPT_TEMPLATE_PATH));
    }
}
