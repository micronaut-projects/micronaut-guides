/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
