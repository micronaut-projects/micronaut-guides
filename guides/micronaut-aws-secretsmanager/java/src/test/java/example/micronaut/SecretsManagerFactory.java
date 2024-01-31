package example.micronaut;

import io.micronaut.context.annotation.BootstrapContextCompatible;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClientBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Factory
@BootstrapContextCompatible
class SecretsManagerFactory {
    private final SecretsManagerConfig secretsManagerConfig;

    SecretsManagerFactory(SecretsManagerConfig secretsManagerConfig) {
        this.secretsManagerConfig = secretsManagerConfig;
    }

    @Primary
    @Singleton
    SecretsManagerClient createSecretsManagerClient(SecretsManagerClientBuilder builder) {
        try {
            return builder
                    .endpointOverride(new URI(secretsManagerConfig.getSecretsManager().getEndpointOverride()))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(secretsManagerConfig.getAccessKeyId(), secretsManagerConfig.getSecretKey())
                            )
                    )
                    .region(Region.of(secretsManagerConfig.getRegion()))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
