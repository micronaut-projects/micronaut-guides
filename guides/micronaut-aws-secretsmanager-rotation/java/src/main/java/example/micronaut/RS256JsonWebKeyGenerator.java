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

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.inject.Singleton;
import java.util.Optional;
import java.util.UUID;

@Singleton // <1>
public class RS256JsonWebKeyGenerator implements JsonWebKeyGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(RS256JsonWebKeyGenerator.class);

    @Override
    @NonNull
    public Optional<String> generateJsonWebKey(@Nullable String kid) {
        try {
            return Optional.of(new RSAKeyGenerator(2048)
                    .algorithm(JWSAlgorithm.RS256)
                    .keyUse(KeyUse.SIGNATURE) // indicate the intended use of the key
                    .keyID(kid != null ? kid : generateKid()) // give the key a unique ID
                    .generate()
                    .toJSONString());

        } catch (JOSEException e) {
            LOG.warn("unable to generate RS256 key",  e);
        }
        return Optional.empty();
    }

    private String generateKid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
