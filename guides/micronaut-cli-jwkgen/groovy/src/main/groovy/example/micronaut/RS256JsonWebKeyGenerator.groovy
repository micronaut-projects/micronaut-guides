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
package example.micronaut

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import jakarta.inject.Singleton

import static com.nimbusds.jose.JWSAlgorithm.RS256
import static com.nimbusds.jose.jwk.KeyUse.SIGNATURE

@Slf4j
@CompileStatic
@Singleton // <1>
class RS256JsonWebKeyGenerator implements JsonWebKeyGenerator {

    @Override
    @NonNull
    Optional<String> generateJsonWebKey(@Nullable String kid) {
        try {
            Optional.of(new RSAKeyGenerator(2048)
                    .algorithm(RS256)
                    .keyUse(SIGNATURE) // indicate the intended use of the key
                    .keyID(kid != null ? kid : generateKid()) // give the key a unique ID
                    .generate()
                    .toJSONString())
        } catch (JOSEException e) {
            log.warn('unable to generate RS256 key', e)
            Optional.empty()
        }
    }

    private String generateKid() {
        UUID.randomUUID().toString().replaceAll('-', '')
    }
}
