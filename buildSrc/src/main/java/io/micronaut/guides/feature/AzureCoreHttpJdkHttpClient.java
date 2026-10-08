package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

/**
 * Azure SDK HTTP client implementation based on the JDK HTTP client.
 */
@Singleton
public class AzureCoreHttpJdkHttpClient extends AbstractFeature {

    public AzureCoreHttpJdkHttpClient() {
        super("azure-core-http-jdk-httpclient");
    }
}
