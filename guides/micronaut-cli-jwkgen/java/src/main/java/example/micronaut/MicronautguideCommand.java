package example.micronaut;

import io.micronaut.configuration.picocli.PicocliRunner;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import jakarta.inject.Inject;

@Command(name = "keysgen",
        description = "Generates a Json Web Key (JWT) with RS256 algorithm.",
        mixinStandardHelpOptions = true)  // <1>
public class MicronautguideCommand implements Runnable {

    @Option(names = {"-kid"}, // <2>
            required = false,
            description = "Key ID. Parameter is used to match a specific key. If not specified a random Key ID is generated.")
    private String kid;

    @Inject
    public JsonWebKeyGenerator jsonWebKeyGenerator; // <3>

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(MicronautguideCommand.class, args);
    }

    public void run() {
        jsonWebKeyGenerator.generateJsonWebKey(kid).ifPresent(this::printlnJsonWebKey);
    }

    private void printlnJsonWebKey(String jwk) {
        System.out.println("JWK: " + jwk);
    }
}