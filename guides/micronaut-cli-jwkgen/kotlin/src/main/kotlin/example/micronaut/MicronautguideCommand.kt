package example.micronaut

import io.micronaut.configuration.picocli.PicocliRunner
import jakarta.inject.Inject
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(name = "keysgen",
         description = ["Generates a Json Web Key (JWT) with RS256 algorithm."],
         mixinStandardHelpOptions = true) // <1>
class MicronautguideCommand : Runnable {

    @Option(names = ["-kid"], // <2>
            required = false,
            description = ["Key ID. Parameter is used to match a specific key. If not specified a random Key ID is generated."])
    private var kid: String? = null

    @Inject
    lateinit var jsonWebKeyGenerator: JsonWebKeyGenerator // <3>

    override fun run() {
        jsonWebKeyGenerator.generateJsonWebKey(kid).ifPresent { jwk: String -> printlnJsonWebKey(jwk) }
    }

    private fun printlnJsonWebKey(jwk: String) {
        println("JWK: $jwk")
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            PicocliRunner.run(MicronautguideCommand::class.java, *args)
        }
    }
}
