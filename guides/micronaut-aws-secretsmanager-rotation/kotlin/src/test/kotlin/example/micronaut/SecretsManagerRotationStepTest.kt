/*
 * Copyright 2017-2026 original authors
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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class SecretsManagerRotationStepTest {

    @ParameterizedTest(name = "for {0} string inferred SecretsManagerRotationStep should be {1}")
    @MethodSource("stepProvider")
    fun shouldResolveRotationStepFromString(str: String, step: SecretsManagerRotationStep) {
        val resolvedStep = SecretsManagerRotationStep.of(str)

        assertTrue(resolvedStep.isPresent)
        assertEquals(step, resolvedStep.get())
    }

    companion object {
        @JvmStatic
        fun stepProvider(): Stream<Arguments> = Stream.of(
            Arguments.of("createSecret", SecretsManagerRotationStep.CREATE_SECRET),
            Arguments.of("setSecret", SecretsManagerRotationStep.SET_SECRET),
            Arguments.of("testSecret", SecretsManagerRotationStep.TEST_SECRET),
            Arguments.of("finishSecret", SecretsManagerRotationStep.FINISH_SECRET)
        )
    }
}
