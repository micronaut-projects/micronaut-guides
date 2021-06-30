package example.micronaut.mock

import com.oracle.bmc.auth.AuthCachingPolicy
import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider
import io.micronaut.context.annotation.Replaces
import java.io.InputStream
import javax.inject.Singleton

@AuthCachingPolicy(cacheKeyId = false, cachePrivateKey = false) // <1>
@Singleton
@Replaces(ConfigFileAuthenticationDetailsProvider::class) // <2>
class MockAuthenticationDetailsProvider : BasicAuthenticationDetailsProvider {
    override fun getKeyId(): String? = null
    override fun getPrivateKey(): InputStream? = null
    override fun getPassPhrase(): String? = null
    override fun getPassphraseCharacters(): CharArray? = null
}
