package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@Property(name = "langchain4j.open-ai.api-key", value = "xxx") // <1>
@MicronautTest(startApplication = false) // <2>
class PromptTemplateTest {

    @Test
    void test(MicronautAiBoardGameService service) {
        String message = service.userMessage(new Question("checkers", "How many pieces are there?"));
        String expectedMessage = """
    You are a helpful assistant, answering questions about tabletop games.
    If you don't know anything about the game or don't know the answer,
    say "I don't know".

    The game is checkers.

    The question is: How many pieces are there?.""";

        assertEquals(expectedMessage, message);
    }
}