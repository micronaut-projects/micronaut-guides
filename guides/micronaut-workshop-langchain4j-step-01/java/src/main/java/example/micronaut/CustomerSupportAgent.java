package example.micronaut;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import io.micronaut.langchain4j.annotation.AiService;

@AiService
public interface CustomerSupportAgent {
    String chat(@MemoryId String memoryId, @UserMessage String userMessage);
}
