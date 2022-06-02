package example.micronaut;
import io.micronaut.function.aws.MicronautRequestHandler;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionRequestHandler extends MicronautRequestHandler<ScheduledEvent, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(FunctionRequestHandler.class);
    @Override
    public Void execute(ScheduledEvent input) {
        LOG.info("input: {}", input);
        return null;
    }
}