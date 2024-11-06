package io.micronaut.guides.core;

import io.micronaut.core.io.ResourceLoader;
import jakarta.inject.Singleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

@Singleton
public class DefaultVersionLoader implements VersionLoader {
    private final String version;

    public DefaultVersionLoader(GuidesConfiguration guidesConfiguration,
                                ResourceLoader resourceLoader) {
        Optional<InputStream> resourceAsStreamOptional = resourceLoader.getResourceAsStream(guidesConfiguration.getVersionPath());
        this.version = resourceAsStreamOptional.map(DefaultVersionLoader::loadVersion).orElse("0.0.0");
    }

    private static String loadVersion(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.readLine();
        } catch (IOException e) {
            throw new UnsupportedOperationException("Cannot load version number: ", e);
        }
    }

    @Override
    public String getVersion() {
        return version;
    }
}
