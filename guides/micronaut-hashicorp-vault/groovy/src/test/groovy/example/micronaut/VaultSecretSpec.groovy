package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration
import org.testcontainers.containers.GenericContainer
import spock.lang.AutoCleanup
import spock.lang.Specification

class VaultSecretSpec extends Specification {

    private static final String ROOT_KEY = "root"

    @AutoCleanup
    GenericContainer vaultContainer = new GenericContainer<>("vault")
            .withExposedPorts(8200)
            .withEnv("VAULT_ADDR", "http://0.0.0.0:8200")
            .withEnv("VAULT_DEV_ROOT_TOKEN_ID", ROOT_KEY)
            .withEnv("VAULT_TOKEN", ROOT_KEY)

    void 'secrets are used for configuration'() {
        when:
        vaultContainer.start()
        addSecret("micronautguide", "hello", "world")

        and:
        def ctx = ApplicationContext.run(
                'vault.client.uri': "http://$vaultContainer.host:" + vaultContainer.getMappedPort(8200),
                'vault.client.token': ROOT_KEY,
        )

        then:
        with(ctx.getBean(OauthClientConfiguration, Qualifiers.byName("companyauthserver"))) {
            clientId == "hello"
            clientSecret == "world"
        }
    }

    def addSecret(String key, String id, String secret) {
        vaultContainer.execInContainer(
                "/bin/sh",
                "-c",
                "vault kv put secret/$key micronaut.security.oauth2.clients.companyauthserver.client-id=$id micronaut.security.oauth2.clients.companyauthserver.client-secret=$secret"
        )
    }
}
