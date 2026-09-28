package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VaultSecretTest {

    private static final String ROOT_KEY = "root";

    GenericContainer<?> vaultContainer = new GenericContainer<>("vault")
            .withExposedPorts(8200)
            .withEnv("VAULT_ADDR", "http://0.0.0.0:8200")
            .withEnv("VAULT_DEV_ROOT_TOKEN_ID", ROOT_KEY)
            .withEnv("VAULT_TOKEN", ROOT_KEY);

    @BeforeAll
    public void setup() throws Exception {
        vaultContainer.start();
        addSecret("micronautguide", "hello", "world");
    }

    @Test
    void secretsAreUsedForConfiguration() {
        ApplicationContext ctx = ApplicationContext.run(
                CollectionUtils.mapOf(
                        "vault.client.uri", "http://" + vaultContainer.getHost() + ":" + vaultContainer.getMappedPort(8200),
                        "vault.client.token", ROOT_KEY
                )
        );

        OauthClientConfiguration oauthClientConfiguration = ctx.getBean(OauthClientConfiguration.class);

        assertEquals("hello", oauthClientConfiguration.getClientId());
        assertEquals("world", oauthClientConfiguration.getClientSecret());

        ctx.close();
    }

    void addSecret(String key, String id, String secret) throws Exception {
        vaultContainer.execInContainer(
                "/bin/sh",
                "-c",
                "vault kv put secret/" + key + " micronaut.security.oauth2.clients.companyauthserver.client-id=" + id + " micronaut.security.oauth2.clients.companyauthserver.client-secret=" + secret
        );
    }

}
