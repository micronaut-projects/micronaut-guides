package example.micronaut;

import com.amazonaws.services.lambda.runtime.events.SecretsManagerRotationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.aws.distributedconfiguration.KeyValueFetcher;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.function.aws.MicronautRequestHandler;
import io.micronaut.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.PutSecretValueRequest;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Introspected
public class FunctionRequestHandler
        extends MicronautRequestHandler<SecretsManagerRotationEvent, Void> { // <1>

    private static final Logger LOG = LoggerFactory.getLogger(FunctionRequestHandler.class);

    private static final String JWK_PRIMARY = "jwk.primary";
    private static final String JWK_SECONDARY = "jwk.secondary";

    @Inject
    public JsonWebKeyGenerator jsonWebKeyGenerator; // <2>

    @Inject
    public JsonMapper objectMapper; // <3>

    @Inject
    public SecretsManagerClient secretsManagerClient; // <4>

    @Inject
    public KeyValueFetcher keyValueFetcher; // <5>

    @Override
    public Void execute(SecretsManagerRotationEvent input) {
        LOG.info("step {} secretId: {}", input.getStep(), input.getSecretId());
        SecretsManagerRotationStep.of(input.getStep()).ifPresent(step -> {
            if (step == SecretsManagerRotationStep.FINISH_SECRET) {
                currentPrimary(input.getSecretId())
                        .flatMap(this::generateSecretString)
                        .ifPresent(secretString -> updateSecretString(input, secretString));
            }
        });
        return null;
    }

    @NonNull
    private Optional<String> currentPrimary(@NonNull String secretId) {  // <6>
        return keyValueFetcher.keyValuesByPrefix(secretId)
                .filter(m -> m.containsKey(JWK_PRIMARY))
                .map(m -> m.get(JWK_PRIMARY))
                .filter(Objects::nonNull)
                .map(Object::toString);
    }



    @NonNull
    private Optional<String> generateSecretString(@NonNull String currentPrimary) {  // <7>
        Optional<String> jsonJwkOptional = jsonWebKeyGenerator.generateJsonWebKey(null);
        if (!jsonJwkOptional.isPresent()) {
            return Optional.empty();
        }
        String jsonJwk = jsonJwkOptional.get();
        Map<String, String> newJwk = new HashMap<>();
        newJwk.put(JWK_PRIMARY, jsonJwk);
        newJwk.put(JWK_SECONDARY, currentPrimary);
        try {
            return Optional.of(objectMapper.writeValueAsString(newJwk));
        } catch (JsonProcessingException e) {
            LOG.warn("JsonProcessingException", e);
        } catch (IOException e) {
            LOG.warn("IOException", e);
        }
        return Optional.empty();
    }

    private void updateSecretString(@NonNull SecretsManagerRotationEvent input,
                                    @NonNull String secretString) {  // <8>
        secretsManagerClient.putSecretValue(PutSecretValueRequest.builder()
                .clientRequestToken(input.getClientRequestToken())
                .secretId(input.getSecretId())
                .secretString(secretString)
                .build());
    }
}
