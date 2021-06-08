package example.micronaut;

import com.amazonaws.services.lambda.runtime.events.SecretsManagerRotationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.aws.distributedconfiguration.KeyValueFetcher;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.function.aws.MicronautRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.PutSecretValueRequest;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Introspected
public class Handler
        extends MicronautRequestHandler<SecretsManagerRotationEvent, Void> { // <1>

    private static final Logger LOG = LoggerFactory.getLogger(Handler.class);

    @Inject
    public JsonWebKeyGenerator jsonWebKeyGenerator; // <3>

    @Inject
    public ObjectMapper objectMapper; // <3>

    @Inject
    public SecretsManagerClient secretsManagerClient; // <4>

    @Inject
    public KeyValueFetcher keyValueFetcher; // <5>

    @Override
    public Void execute(SecretsManagerRotationEvent input) {
        if (LOG.isInfoEnabled()) {
            LOG.info("step {} secretId: {}", input.getStep(), input.getSecretId());
        }
        SecretsManagerRotationStep.of(input.getStep()).ifPresent(step -> {
            if (step == SecretsManagerRotationStep.FINISH_SECRET) {
                currentPrimary(input.getSecretId())
                        .flatMap(this::generateSecretString)
                        .ifPresent(secretString -> secretsManagerClient.putSecretValue(PutSecretValueRequest.builder()
                                .clientRequestToken(input.getClientRequestToken())
                                .secretId(input.getSecretId())
                                .secretString(secretString)
                                .build()));
            }
        });
        return null;
    }

    @NonNull
    private Optional<String> generateSecretString(@NonNull String currentPrimary) {
        Optional<String> jsonJwkOptional = jsonWebKeyGenerator.generateJsonWebKey(null);
        if (!jsonJwkOptional.isPresent()) {
            return Optional.empty();
        }
        String jsonJwk = jsonJwkOptional.get();
        Map<String, String> newJwk = new HashMap<>();
        newJwk.put("jwk.primary", jsonJwk);
        newJwk.put("jwk.secondary", currentPrimary);
        try {
            return Optional.of(objectMapper.writeValueAsString(newJwk));
        } catch (JsonProcessingException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("JsonProcessingException", e);
            }
        }
        return Optional.empty();
    }

    @NonNull
    private Optional<String> currentPrimary(@NonNull String secretId) {
        return keyValueFetcher.keyValuesByPrefix(secretId)
                .filter(m -> m.containsKey("jwk.primary"))
                .map(m -> m.get("jwk.primary"))
                .filter(Objects::nonNull)
                .map(Object::toString);
    }
}
