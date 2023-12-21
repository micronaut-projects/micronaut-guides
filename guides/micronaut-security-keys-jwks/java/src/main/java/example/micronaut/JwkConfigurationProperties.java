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

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;

import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties("jwk") // <1>
public class JwkConfigurationProperties implements JwkConfiguration {

    @NonNull
    @NotBlank // <2>
    private String primary;

    @NonNull
    @NotBlank // <2>
    private String secondary;

    @Override
    @NonNull
    public String getPrimary() {
        return primary;
    }

    @Override
    @NonNull
    public String getSecondary() {
        return secondary;
    }

    public void setPrimary(@NonNull String primary) {
        this.primary = primary;
    }

    public void setSecondary(@NonNull String secondary) {
        this.secondary = secondary;
    }
}
