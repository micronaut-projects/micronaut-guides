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
package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

public enum SecretsManagerRotationStep {

    CREATE_SECRET("createSecret"),
    SET_SECRET("setSecret"),
    TEST_SECRET("testSecret"),
    FINISH_SECRET("finishSecret");

    private static final Map<String, SecretsManagerRotationStep> ENUM_MAP;

    static {
        Map<String, SecretsManagerRotationStep> map = new HashMap<>();
        for (SecretsManagerRotationStep instance : SecretsManagerRotationStep.values()) {
            map.put(instance.toString(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    private final String step;

    SecretsManagerRotationStep(String step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return this.step;
    }

    @NonNull
    public static Optional<SecretsManagerRotationStep> of(@NonNull String step) {
        return Optional.ofNullable(ENUM_MAP.get(step));
    }
}
