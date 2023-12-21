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
package example.micronaut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;

class SecretsManagerRotationStepTest {

    @ParameterizedTest(name = "for {0} string inferred SecretsManagerRotationStep should be {1}")
    @MethodSource("stepProvider")
    void shouldCalculateToPayValueForInvoice(String str, SecretsManagerRotationStep step) {
        assertTrue(SecretsManagerRotationStep.of(str).isPresent());
        assertEquals(SecretsManagerRotationStep.of(str).get(), step);
    }

    private static Stream<Arguments> stepProvider() {
        return Stream.of(
                Arguments.of("createSecret", SecretsManagerRotationStep.CREATE_SECRET),
                Arguments.of("setSecret", SecretsManagerRotationStep.SET_SECRET),
                Arguments.of("testSecret", SecretsManagerRotationStep.TEST_SECRET),
                Arguments.of("finishSecret", SecretsManagerRotationStep.FINISH_SECRET)
        );
    }
}
