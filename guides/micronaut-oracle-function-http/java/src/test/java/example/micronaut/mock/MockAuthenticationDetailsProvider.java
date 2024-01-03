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
package example.micronaut.mock;

import com.oracle.bmc.auth.AuthCachingPolicy;
import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import io.micronaut.context.annotation.Replaces;
import jakarta.inject.Singleton;

import java.io.InputStream;

@AuthCachingPolicy(cacheKeyId = false, cachePrivateKey = false) // <1>
@Singleton
@Replaces(ConfigFileAuthenticationDetailsProvider.class) // <2>
public class MockAuthenticationDetailsProvider implements BasicAuthenticationDetailsProvider {

    @Override
    public String getKeyId() {
        return null;
    }

    @Override
    public InputStream getPrivateKey() {
        return null;
    }

    @Override
    public String getPassPhrase() {
        return null;
    }

    @Override
    public char[] getPassphraseCharacters() {
        return null;
    }
}
