package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@Introspected
public class Handler extends MicronautRequestHandler<Map<String, Object>, Void> {

    private static final Logger LOG = LoggerFactory.getLogger(Handler.class);

    @Override
    public Void execute(Map<String, Object> input) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("{}", input);
        }
        return null;
    }


}
