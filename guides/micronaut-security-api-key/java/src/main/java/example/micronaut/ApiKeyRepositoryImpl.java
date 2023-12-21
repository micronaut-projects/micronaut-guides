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

import jakarta.inject.Singleton;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

@Singleton // <1>
class ApiKeyRepositoryImpl implements ApiKeyRepository {

    private final Map<String, Principal> keys;

    ApiKeyRepositoryImpl(List<ApiKeyConfiguration> apiKeys) {
        keys = new HashMap<>();
        for (ApiKeyConfiguration configuration : apiKeys) {
            keys.put(configuration.getKey(), configuration::getName);
        }
        System.out.println("Keys #" + keys.keySet().size());
    }

    @Override
    @NonNull
    public Optional<Principal> findByApiKey(@NonNull @NotBlank String apiKey) {
        return Optional.ofNullable(keys.get(apiKey));
    }
}
