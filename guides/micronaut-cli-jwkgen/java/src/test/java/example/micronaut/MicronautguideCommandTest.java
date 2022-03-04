package example.micronaut;

import com.nimbusds.jose.jwk.JWK;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MicronautguideCommandTest {

    @Test
    void testGenerateJwk() {
        final String jwkJsonRepresentation = executeCommand(MicronautguideCommand.class, new String[]{});
        assertNotNull(jwkJsonRepresentation);

        String prefix = "JWK: ";
        assertNotEquals(jwkJsonRepresentation.indexOf(prefix), -1);
        assertDoesNotThrow(() ->
                JWK.parse(jwkJsonRepresentation.substring(jwkJsonRepresentation.indexOf(prefix) + prefix.length()))
        );
    }

    private String executeCommand(Class commandClass, String[] args) { // <1>
        OutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            PicocliRunner.run(commandClass, ctx, args);
        }
        return baos.toString();
    }
}
