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
