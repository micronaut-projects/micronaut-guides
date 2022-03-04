package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import spock.lang.Specification

class JsonWebKeyGeneratorSpec extends Specification {

    void beanOfTypeJsonWebKeyGeneratorExists() {
        when:
        ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

        then:
        ctx.containsBean(JsonWebKeyGenerator)

        cleanup:
        ctx?.close()
    }
}
