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
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;

@Requires(property = "app.hosted-domain")
@ConfigurationProperties("app")
public class ApplicationConfigurationProperties implements ApplicationConfiguration {

    @NonNull
    private String hostedDomain;

    public void setHostedDomain(@NonNull String hostedDomain) {
        this.hostedDomain = hostedDomain;
    }

    @Override
    @NonNull
    public String getHostedDomain() {
        return hostedDomain;
    }
}
