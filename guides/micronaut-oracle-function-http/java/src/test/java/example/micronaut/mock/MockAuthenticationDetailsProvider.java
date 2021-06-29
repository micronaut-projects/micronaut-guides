package example.micronaut.mock;

import com.oracle.bmc.auth.AuthCachingPolicy;
import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import io.micronaut.context.annotation.Replaces;

import javax.inject.Singleton;
import java.io.InputStream;

@AuthCachingPolicy(cacheKeyId = false, cachePrivateKey = false)
@Singleton
@Replaces(ConfigFileAuthenticationDetailsProvider.class)
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
