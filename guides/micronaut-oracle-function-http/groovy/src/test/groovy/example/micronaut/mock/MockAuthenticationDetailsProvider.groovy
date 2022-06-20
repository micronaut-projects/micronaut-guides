package example.micronaut.mock

import com.oracle.bmc.auth.AuthCachingPolicy
import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider
import io.micronaut.context.annotation.Replaces

import jakarta.inject.Singleton

@AuthCachingPolicy(cacheKeyId = false, cachePrivateKey = false) // <1>
@Singleton
@Replaces(ConfigFileAuthenticationDetailsProvider) // <2>
class MockAuthenticationDetailsProvider implements BasicAuthenticationDetailsProvider {
    String keyId
    InputStream privateKey
    String passPhrase
    char[] passphraseCharacters
}
