package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class JsonWebKeyGeneratorTest {

    @Test
    fun beanOfTypeJsonWebKeyGeneratorExists() {
        ApplicationContext.run(Environment.CLI, Environment.TEST).use { ctx ->
            assertTrue(ctx.containsBean(JsonWebKeyGenerator::class.java))
        }
    }
}
