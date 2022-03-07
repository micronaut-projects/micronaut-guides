package example.micronaut

import com.nimbusds.jose.jwk.JWK
import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import spock.lang.Specification

class MicronautguideCommandSpec extends Specification {

    void testGenerateJwk() {
        when:
        String jwkJsonRepresentation = executeCommand(MicronautguideCommand, [] as String[])

        then:
        jwkJsonRepresentation != null

        when:
        String prefix = 'JWK: '

        then:
        jwkJsonRepresentation.indexOf(prefix) > -1

        when:
        JWK.parse(jwkJsonRepresentation.substring(jwkJsonRepresentation.indexOf(prefix) + prefix.length()))

        then:
        noExceptionThrown()
    }

    private String executeCommand(Class commandClass, String[] args) { // <1>
        OutputStream baos = new ByteArrayOutputStream()
        System.setOut(new PrintStream(baos))
        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            PicocliRunner.run(commandClass, ctx, args)
        }
        baos
    }
}
