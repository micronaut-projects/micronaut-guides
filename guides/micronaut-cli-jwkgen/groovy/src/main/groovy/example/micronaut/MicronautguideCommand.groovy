package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.configuration.picocli.PicocliRunner
import jakarta.inject.Inject
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(name = 'keysgen',
         description = 'Generates a Json Web Key (JWT) with RS256 algorithm.',
         mixinStandardHelpOptions = true)  // <1>
@CompileStatic
class MicronautguideCommand implements Runnable {

    @Option(names = ['-kid'], // <2>
            required = false,
            description = 'Key ID. Parameter is used to match a specific key. If not specified a random Key ID is generated.')
    private String kid

    @Inject
    JsonWebKeyGenerator jsonWebKeyGenerator // <3>

    static void main(String[] args) {
        PicocliRunner.run(MicronautguideCommand, args)
    }

    void run() {
        jsonWebKeyGenerator.generateJsonWebKey(kid).ifPresent(this::printlnJsonWebKey)
    }

    private void printlnJsonWebKey(String jwk) {
        println('JWK: ' + jwk)
    }
}
