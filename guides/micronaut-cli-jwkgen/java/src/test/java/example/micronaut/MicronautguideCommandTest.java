package example.micronaut;

import com.nimbusds.jose.jwk.JWK;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MicronautguideCommandTest {

    @Test
    public void testGenerateJwk() throws Exception {
        final String jwkJsonRepresentation = executeCommand(MicronautguideCommand.class, new String[] {});
        assertNotNull(jwkJsonRepresentation);
        String prefix = "JWK: ";
        assertNotEquals(jwkJsonRepresentation.indexOf(prefix), -1);
        assertDoesNotThrow(() -> JWK.parse(jwkJsonRepresentation.substring(jwkJsonRepresentation.indexOf(prefix) + prefix.length())));
    }

    String executeCommand(Class commandClass, String[] args) { // <1>
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            PicocliRunner.run(commandClass, ctx, args);
        }
        return baos.toString();
    }
}
