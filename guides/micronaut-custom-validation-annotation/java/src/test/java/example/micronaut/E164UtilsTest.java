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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class E164UtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "+04630443322",
            "+1415555267102345",
            "+1-4155552671",
            ""
    })
    void invalidPhones(String phone) {
        assertFalse(E164Utils.isValid(phone));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "+14155552671",
            "+442071838750",
            "+55115525632",
            "14155552671",
            "442071838750",
            "55115525632",
            "55115525632",
    })
    void validPhones(String phone) {
        assertTrue(E164Utils.isValid(phone));
    }
}
