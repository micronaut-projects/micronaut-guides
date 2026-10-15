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

import java.util.Optional

enum class SecretsManagerRotationStep(private val step: String) {

    CREATE_SECRET("createSecret"),
    SET_SECRET("setSecret"),
    TEST_SECRET("testSecret"),
    FINISH_SECRET("finishSecret");

    override fun toString(): String = step

    companion object {
        private val enumMap: Map<String, SecretsManagerRotationStep> = entries.associateBy { it.toString() }

        fun of(step: String): Optional<SecretsManagerRotationStep> =
            Optional.ofNullable(enumMap[step])
    }
}
