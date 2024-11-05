package io.micronaut.guides.core;

import com.networknt.schema.InputFormat;
import com.networknt.schema.ValidationMessage;
import groovy.json.JsonSlurper;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.json.JsonMapper;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static io.micronaut.guides.core.GuideUtils.mergeMetadataList;

@Singleton
public class DefaultGuideParser implements GuideParser {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultGuideParser.class);

    private final JsonSchemaProvider jsonSchemaProvider;
    private final JsonMapper jsonMapper;

    public DefaultGuideParser(JsonSchemaProvider jsonSchemaProvider, JsonMapper jsonMapper) {
        this.jsonSchemaProvider = jsonSchemaProvider;
        this.jsonMapper = jsonMapper;

    }

    @Override
    public List<Guide> parseGuidesMetadata(@NonNull @NotNull File guidesDir, @NonNull @NotNull String metadataConfigName) {
        List<Guide> metadatas = new ArrayList<>();

        File dirs[] = guidesDir.listFiles(File::isDirectory);
        if(dirs == null) {
            return metadatas;
        }
        for (File dir : dirs) {
            parseGuideMetadata(dir, metadataConfigName).ifPresent(metadatas::add);
        }

        mergeMetadataList(metadatas);

        return metadatas;
    }

    @Override
    public Optional<Guide> parseGuideMetadata(@NonNull @NotNull File guidesDir, @NonNull @NotNull String metadataConfigName) {
        File configFile = new File(guidesDir, metadataConfigName);
        if (!configFile.exists()) {
            LOG.warn("metadata file not found for {}", guidesDir.getName());
            return Optional.empty();
        }

        String content;
        try {
            content = Files.readString(Paths.get(configFile.toString()));
        } catch (IOException e) {
            LOG.warn("metadata file not found for {}", guidesDir.getName());
            return Optional.empty();
        }

        Map<String, Object> config = (Map<String,Object>) new JsonSlurper().parse(configFile);
        boolean publish = config.get("publish") == null || (Boolean) config.get("publish");

        if(publish) {
            Set<ValidationMessage> assertions = jsonSchemaProvider.getSchema().validate(content, InputFormat.JSON);

            if (!assertions.isEmpty()) {
                LOG.trace("Guide metadata {} does not validate the JSON Schema. Skipping guide.", configFile);
                return Optional.empty();
            }
        }

        Guide raw;
        try {
            raw = jsonMapper.readValue(content, Guide.class);
        } catch (IOException e) {
            LOG.trace("Error parsing guide metadata {}. Skipping guide.", configFile, e);
            return Optional.empty();
        }

        List<App> apps = new LinkedList<>();

        for (App app : raw.apps()) {
            apps.add(new App(
                    app.name(),
                    app.packageName(),
                    app.applicationType(),
                    app.framework(),
                    app.features() != null ? app.features() : new ArrayList<>(),
                    app.invisibleFeatures() != null ? app.invisibleFeatures() : new ArrayList<>(),
                    app.kotlinFeatures() != null ? app.kotlinFeatures() : new ArrayList<>(),
                    app.javaFeatures() != null ? app.javaFeatures() : new ArrayList<>(),
                    app.groovyFeatures() != null ? app.groovyFeatures() : new ArrayList<>(),
                    app.testFramework(),
                    app.excludeTest(),
                    app.excludeSource(),
                    app.validateLicense()
            ));
        }

        return Optional.of(new Guide(
                raw.title(),
                raw.intro(),
                raw.authors(),
                raw.categories(),
                publish ? raw.publicationDate() : null,
                raw.minimumJavaVersion(),
                raw.maximumJavaVersion(),
                raw.cloud(),
                raw.skipGradleTests(),
                raw.skipMavenTests(),
                publish ? guidesDir.getName() + ".adoc" : null,
                raw.languages() != null ? raw.languages() : List.of(Language.JAVA, Language.GROOVY, Language.KOTLIN),
                raw.tags() != null ? raw.tags() : Collections.emptyList(),
                raw.buildTools() != null ? raw.buildTools() : List.of(BuildTool.GRADLE, BuildTool.MAVEN),
                raw.testFramework(),
                raw.zipIncludes() != null ? raw.zipIncludes() : new ArrayList<>(),
                guidesDir.getName(),
                publish,
                raw.base(),
                raw.env() != null ? raw.env() : new HashMap<>(),
                apps
        ));
    }
}
