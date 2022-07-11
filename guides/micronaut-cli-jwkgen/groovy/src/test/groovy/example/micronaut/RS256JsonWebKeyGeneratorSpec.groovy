package example.micronaut

import com.nimbusds.jose.jwk.JWK
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import spock.lang.Specification

class RS256JsonWebKeyGeneratorSpec extends Specification {

    void generatesJWK() {
        when:
        ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

        then:
        ctx.containsBean(RS256JsonWebKeyGenerator)

        when:
        RS256JsonWebKeyGenerator generator = ctx.getBean(RS256JsonWebKeyGenerator)
        Optional<String> jwkJsonOptional = generator.generateJsonWebKey(null)

        then:
        jwkJsonOptional.isPresent()

        when:
        String jwkJson = jwkJsonOptional.get()
        JWK.parse(jwkJson)

        then:
        noExceptionThrown()
    }
}
