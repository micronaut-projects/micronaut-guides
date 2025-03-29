package example.micronaut;

import com.amazonaws.services.lambda.runtime.events.CognitoUserPoolPostAuthenticationEvent;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Introspected
public class Handler extends MicronautRequestHandler<CognitoUserPoolPostAuthenticationEvent, Void> {

    @Inject
    AnalyticsClient analyticsClient;

    private static final Logger LOG = LoggerFactory.getLogger(Handler.class);
    @Override
    public Void execute(CognitoUserPoolPostAuthenticationEvent input) {
        LOG.info("UserName {}", input.getUserName());
        analyticsClient.authenticated(input.getUserName());
        return null;
    }
}
