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
import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PrintStream

class MicronautguideCommandTest {

    @Test
    fun testGenerateJwk() {
        val jwkJsonRepresentation = executeCommand(MicronautguideCommand::class.java, arrayOf())
        assertNotNull(jwkJsonRepresentation)

        val prefix = "JWK: "
        assertNotEquals(jwkJsonRepresentation.indexOf(prefix), -1)
        assertDoesNotThrow<JWK> {
            JWK.parse(jwkJsonRepresentation.substring(jwkJsonRepresentation.indexOf(prefix) + prefix.length))
        }
    }

    private fun executeCommand(commandClass: Class<MicronautguideCommand>, args: Array<String>): String { // <1>
        val baos: OutputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(baos))
        ApplicationContext.run(Environment.CLI, Environment.TEST).use { ctx ->
            PicocliRunner.run(commandClass, ctx, *args)
        }
        return baos.toString()
    }
}
