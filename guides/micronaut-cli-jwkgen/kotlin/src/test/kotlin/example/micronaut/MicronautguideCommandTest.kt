package example.micronaut

import com.nimbusds.jose.jwk.JWK
import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PrintStream

class MicronautguideCommandTest {

    @Test
    fun testGenerateJwk() {
        val jwkJsonRepresentation = executeCommand(MicronautguideCommand::class.java, arrayOf())
        assertNotNull(jwkJsonRepresentation)

        val prefix = "JWK: "
        assertNotEquals(jwkJsonRepresentation.indexOf(prefix), -1)
        assertDoesNotThrow<JWK> {
            JWK.parse(jwkJsonRepresentation.substring(jwkJsonRepresentation.indexOf(prefix) + prefix.length))
        }
    }

    private fun executeCommand(commandClass: Class<MicronautguideCommand>, args: Array<String>): String { // <1>
        val baos: OutputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(baos))
        ApplicationContext.run(Environment.CLI, Environment.TEST).use { ctx ->
            PicocliRunner.run(commandClass, ctx, *args)
        }
        return baos.toString()
    }
}
