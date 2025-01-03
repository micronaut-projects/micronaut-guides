package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.Collections;
import java.util.Map;

@Controller // <1>
class HelloWorldController {

    private final MessageRepository messageRepository;

    HelloWorldController(MessageRepository messageRepository) { // <2>
        this.messageRepository = messageRepository;
    }

    @Get // <3>
    Map<String, String> index() {
        return messageRepository.findAll()
                .stream().map(entity ->
                        Collections.singletonMap("message", entity.message()))
                .findFirst()
                .orElseGet(Collections::emptyMap);
    }
}
