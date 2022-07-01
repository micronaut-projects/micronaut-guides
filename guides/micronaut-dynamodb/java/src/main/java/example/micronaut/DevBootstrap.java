package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;

@Requires(property = "dynamodb-local.host") // <1>
@Requires(property = "dynamodb-local.port") // <2>
@Requires(env = Environment.DEVELOPMENT) // <3>
@Singleton // <4>
public class DevBootstrap implements ApplicationEventListener<StartupEvent> {

    private final DynamoRepository dynamoRepository;

    public DevBootstrap(DynamoRepository dynamoRepository) {
        this.dynamoRepository = dynamoRepository;
    }

    @Override
    public void onApplicationEvent(StartupEvent event) {
        if (!dynamoRepository.existsTable()) {
            dynamoRepository.createTable();
        }
    }
}
