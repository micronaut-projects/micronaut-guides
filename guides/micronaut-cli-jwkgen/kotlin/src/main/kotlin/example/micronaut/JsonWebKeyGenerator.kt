package example.micronaut

import java.util.Optional

/**
 * [JSON Web Key](https://datatracker.ietf.org/doc/html/rfc7517)
 */
interface JsonWebKeyGenerator {

    fun generateJsonWebKey(kid: String?): Optional<String>
}
