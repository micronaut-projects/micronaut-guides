package io.micronaut.guides.core;

import io.micronaut.core.io.ResourceLoader;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Optional;

@Singleton
public class DefaultLicenseLoader implements LicenseLoader {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultLicenseLoader.class);
    private final int numberOfLines;
    private final String licenseHeaderText;

    public DefaultLicenseLoader(GuidesConfiguration guidesConfiguration,
                                ResourceLoader resourceLoader) {
        Optional<InputStream> resourceAsStreamOptional = resourceLoader.getResourceAsStream(guidesConfiguration.getLicensePath());
        this.licenseHeaderText = resourceAsStreamOptional.map(DefaultLicenseLoader::readLicenseHeader).orElse("");
        this.numberOfLines = (int) licenseHeaderText.lines().count() + 1;
    }

    private static int countLines(InputStream inputStream) {
        int numberOfLines = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            numberOfLines = (int) reader.lines().count() + 1;
        } catch (IOException e) {
            LOG.error("", e);
        }
        return numberOfLines;
    }

    private static String readLicenseHeader(InputStream inputStream) {
        StringBuilder licenseHeaderText = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            reader.lines().forEach(line -> licenseHeaderText.append(line).append("\n"));
        } catch (IOException e) {
            LOG.error("", e);
        }
        return licenseHeaderText.toString().replace("$YEAR", String.valueOf(LocalDate.now().getYear()));
    }

    @Override
    public int getNumberOfLines() {
        return this.numberOfLines;
    }

    @Override
    public String getLicenseHeaderText() { return this.licenseHeaderText; }
}
