package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.GenericContainer

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VaultSecretTest {

    companion object  {
        private const val  ROOT_KEY = "root"
    }

    val vaultContainer = GenericContainer("vault")
        .withExposedPorts(8200)
        .withEnv("VAULT_ADDR", "http://0.0.0.0:8200")
        .withEnv("VAULT_DEV_ROOT_TOKEN_ID", ROOT_KEY)
        .withEnv("VAULT_TOKEN", ROOT_KEY)

    @BeforeAll
    fun setup(){
        vaultContainer.start()
        addSecret("micronautguide", "hello", "world")
    }

    @Test
    fun secretsAreUsedForConfiguration(){
        val ctx: ApplicationContext = ApplicationContext.run(
            mapOf(
                "vault.client.uri" to "http://${vaultContainer.host}:${vaultContainer.getMappedPort(8200)}",
                "vault.client.token" to ROOT_KEY
            )
        )

        val oauthClientConfiguration = ctx.getBean(OauthClientConfiguration::class.java)

        assertEquals("hello", oauthClientConfiguration.clientId)
        assertEquals("world", oauthClientConfiguration.clientSecret)

        ctx.close()
    }

    private fun addSecret(key: String, id: String, secret: String){
        vaultContainer.execInContainer(
            "/bin/sh",
            "-c",
            "vault kv put secret/$key micronaut.security.oauth2.clients.companyauthserver.client-id=$id micronaut.security.oauth2.clients.companyauthserver.client-secret=$secret"
        )
    }
}
