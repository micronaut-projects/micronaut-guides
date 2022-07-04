package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;

@Requires(property = "dynamodb-local.host") // <1>
@Requires(property = "dynamodb-local.port") // <1>
@Requires(env = Environment.DEVELOPMENT) // <2>
@Singleton // <3>
public class DevBootstrap implements ApplicationEventListener<StartupEvent> {

    private final DynamoRepository<? extends Identified> dynamoRepository;

    public DevBootstrap(DynamoRepository<? extends Identified> dynamoRepository) {
        this.dynamoRepository = dynamoRepository;
    }

    @Override
    public void onApplicationEvent(StartupEvent event) {
        if (!dynamoRepository.existsTable()) {
            dynamoRepository.createTable();
        }
    }
}
