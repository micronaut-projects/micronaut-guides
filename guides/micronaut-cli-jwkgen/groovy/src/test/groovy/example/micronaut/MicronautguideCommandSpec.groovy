/*
 * Copyright 2017-2023 original authors
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
