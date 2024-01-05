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
