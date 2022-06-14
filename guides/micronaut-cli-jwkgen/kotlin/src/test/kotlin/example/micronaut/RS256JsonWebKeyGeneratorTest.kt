package example.micronaut

import com.nimbusds.jose.jwk.JWK
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RS256JsonWebKeyGeneratorTest {

    @Test
    fun generatesJWK() {
        ApplicationContext.run(Environment.CLI, Environment.TEST).use { ctx ->
            assertTrue(ctx.containsBean(RS256JsonWebKeyGenerator::class.java))

            val generator = ctx.getBean(RS256JsonWebKeyGenerator::class.java)
            val jwkJsonOptional = generator.generateJsonWebKey(null)
            assertTrue(jwkJsonOptional.isPresent)

            val jwkJson = jwkJsonOptional.get()
            assertDoesNotThrow<JWK> { JWK.parse(jwkJson) }
        }
    }
}
