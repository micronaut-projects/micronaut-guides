package example.micronaut;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;

@Singleton // <1>
public class ServerStartupEventListener implements ApplicationEventListener<ServerStartupEvent> { // <2>

    private final MessageRepository messageRepository;

    public ServerStartupEventListener(MessageRepository messageRepository) { // <3>
        this.messageRepository = messageRepository;
    }

    @Override
    public void onApplicationEvent(ServerStartupEvent event) { // <4>
        if (messageRepository.count() == 0) {
            messageRepository.save(new Message(null, "Hello World"));
        }
    }
}
