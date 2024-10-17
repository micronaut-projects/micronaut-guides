package io.micronaut.guides.core;

import com.networknt.schema.*;
import groovy.json.DefaultJsonGenerator;
import groovy.json.JsonSlurper;
import io.micronaut.json.JsonMapper;
import io.micronaut.serde.ObjectMapper;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;

import io.micronaut.starter.options.Language;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public final class GuideUtils {

    private static final Logger LOG = LoggerFactory.getLogger(GuideUtils.class);

    private static final String MICRONAUT_PREFIX = "micronaut-";
    private static final String VIEWS_PREFIX = "views-";
    private static final List<String> FEATURES_PREFIXES = List.of(MICRONAUT_PREFIX, VIEWS_PREFIX);
    private static final String FEATURE_SPOTLESS = "spotless";

    private JsonSchema schema;

    JsonMapper jsonMapper;

    public GuideUtils() {
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012, builder ->
                builder.schemaMappers(schemaMappers -> schemaMappers.mapPrefix("https://www.guides.micronaut.io/schemas", "classpath:META-INF/schemas"))
        );

        SchemaValidatorsConfig.Builder builder = SchemaValidatorsConfig.builder();
        SchemaValidatorsConfig validatorsConfig = builder.build();
        schema = jsonSchemaFactory.getSchema(SchemaLocation.of("https://www.guides.micronaut.io/schemas/guide-metadata.schema.json"), validatorsConfig);
        jsonMapper = JsonMapper.createDefault();
    }

    public List<Guide> parseGuidesMetadata(File guidesDir, String metadataConfigName) throws Exception {
        List<Guide> metadatas = new ArrayList<>();

        for (File dir : guidesDir.listFiles(File::isDirectory)) {
            parseGuideMetadata(dir, metadataConfigName).ifPresent(metadatas::add);
        }

        mergeMetadataList(metadatas);

        return metadatas;
    }

    Optional<Guide> parseGuideMetadata(File dir, String metadataConfigName) throws Exception {
        File configFile = new File(dir, metadataConfigName);
        if (!configFile.exists()) {
            LOG.warn("metadata file not found for {}", dir.getName());
            return Optional.empty();
        }

        String content;
        try {
            content = Files.readString(Paths.get(configFile.toString()));
        } catch (IOException e) {
            LOG.warn("metadata file not found for {}", dir.getName());
            return Optional.empty();
        }

        Map<String, Object> config = (Map<String,Object>) new JsonSlurper().parse(configFile);
        boolean publish = config.get("publish") == null ? true : (Boolean) config.get("publish");

        if(publish){
            Set<ValidationMessage> assertions = schema.validate(content, InputFormat.JSON);

            if (!assertions.isEmpty()) {
                throw new Exception("Guide metadata " + configFile + " does not validate the JSON Schema" + assertions);
            }
        }

        Guide raw = jsonMapper.readValue(content, Guide.class);

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

        return Optional.ofNullable(new Guide(
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
                publish ? dir.getName() + ".adoc" : null,
                raw.languages() != null ? raw.languages() : List.of(Language.JAVA, Language.GROOVY, Language.KOTLIN),
                raw.tags(),
                raw.buildTools() != null ? raw.buildTools() : List.of(BuildTool.GRADLE, BuildTool.MAVEN),
                raw.testFramework(),
                raw.zipIncludes() != null ? raw.zipIncludes() : new ArrayList<>(),
                dir.getName(),
                publish,
                raw.base(),
                raw.env() != null ? raw.env() : new HashMap<>(),
                apps
        ));
    }

    public static List<String> getTags(Guide guide) {
        Set<String> tagsList = new HashSet<>();
        if (guide.tags() != null) {
            addAllSafe(tagsList, guide.tags());
        }
        for (App app : guide.apps()) {
            List<String> allFeatures = new ArrayList<>();
            addAllSafe(allFeatures,app.features());
            addAllSafe(allFeatures,app.javaFeatures());
            addAllSafe(allFeatures,app.kotlinFeatures());
            addAllSafe(allFeatures,app.groovyFeatures());
            for (String featureName : allFeatures) {
                String tagToAdd = featureName;
                for (String prefix : FEATURES_PREFIXES) {
                    if (tagToAdd.startsWith(prefix)) {
                        tagToAdd = tagToAdd.substring(prefix.length());
                    }
                }
                tagsList.add(tagToAdd);
            }
        }
        Set<String> categoriesAsTags = guide.categories().stream().map(String::toLowerCase).map(s -> s.replace(" ","-")).collect(Collectors.toSet());
        tagsList.addAll(categoriesAsTags);
        return tagsList.stream().collect(Collectors.toList());
    }

    public static List<String> getAppFeatures(App app, Language language) {
        if (language == Language.JAVA) {
            return mergeLists(app.features(), getAppInvisibleFeatures(app), app.javaFeatures());
        }
        if (language == Language.KOTLIN) {
            return mergeLists(app.features(), getAppInvisibleFeatures(app), app.kotlinFeatures());
        }
        if (language == Language.GROOVY) {
            return mergeLists(app.features(), getAppInvisibleFeatures(app), app.groovyFeatures());
        }
        return mergeLists(app.features(), getAppInvisibleFeatures(app));
    }

    public static List<String> getAppInvisibleFeatures(App app) {
        if (app.validateLicense()) {
            List<String> result = new ArrayList<>();
            addAllSafe(result,app.invisibleFeatures());
            result.add(FEATURE_SPOTLESS);
            return result;
        }
        return app.invisibleFeatures();
    }

    public static List<String> getAppVisibleFeatures(App app, Language language) {
        if (language == Language.JAVA) {
            return mergeLists(app.features(),app.javaFeatures());
        }
        if (language == Language.KOTLIN) {
            return mergeLists(app.features(),app.kotlinFeatures());
        }
        if (language == Language.GROOVY) {
            return mergeLists(app.features(),app.groovyFeatures());
        }
        return app.features();
    }

    public static boolean shouldSkip(Guide guide, BuildTool buildTool) {
        if (BuildTool.valuesGradle().contains(buildTool)) {
            return guide.skipGradleTests();
        }
        if (buildTool == BuildTool.MAVEN) {
            return guide.skipMavenTests();
        }
        return false;
    }

    public static Set<String> getFrameworks(Guide guide){
        return guide.apps().stream().map(it -> it.framework()).collect(Collectors.toSet());
    }

    public static Guide merge(Guide base, Guide guide) {
        Guide merged = new Guide(
                guide.title() == null ? base.title() : guide.title(),
                guide.intro() == null ? base.intro() : guide.intro(),
                mergeLists(base.authors(), guide.authors()),
                guide.categories() == null ? base.categories() : guide.categories(),
                guide.publicationDate(),
                guide.minimumJavaVersion() == null ? base.minimumJavaVersion() : guide.minimumJavaVersion(),
                guide.maximumJavaVersion() == null ? base.maximumJavaVersion() : guide.maximumJavaVersion(),
                guide.cloud() == null ? base.cloud() : guide.cloud(),
                base.skipGradleTests() || guide.skipGradleTests(),
                base.skipMavenTests() || guide.skipMavenTests(),
                guide.asciidoctor(),
                guide.languages() == null ? base.languages() : guide.languages(),
                mergeLists(base.tags(), guide.tags()),
                guide.buildTools() == null ? base.buildTools() : guide.buildTools(),
                guide.testFramework() == null ? base.testFramework() : guide.testFramework(),
                guide.zipIncludes(),
                guide.slug(),
                guide.publish(),
                guide.base(),
                guide.env() == null ? base.env() : guide.env(),
                mergeApps(base.apps(),guide.apps())
        );
        return merged;
    }

    private static List<App> mergeApps(List<App> base, List<App> guide) {
        Map<String, App> baseApps = base.stream()
                .collect(Collectors.toMap(App::name, app -> app));

        Map<String, App> guideApps = guide.stream()
                .collect(Collectors.toMap(App::name, app -> app));

        Set<String> baseOnly = new HashSet<>(baseApps.keySet());
        baseOnly.removeAll(guideApps.keySet());

        Set<String> guideOnly = new HashSet<>(guideApps.keySet());
        guideOnly.removeAll(baseApps.keySet());

        Set<String> inBoth = new HashSet<>(baseApps.keySet());
        inBoth.retainAll(guideApps.keySet());

        List<App> merged = new ArrayList<>();
        merged.addAll(baseOnly.stream()
                .map(baseApps::get)
                .collect(Collectors.toList()));
        merged.addAll(guideOnly.stream()
                .map(guideApps::get)
                .collect(Collectors.toList()));

        for (String name : inBoth) {
            App baseApp = baseApps.get(name);
            App guideApp = guideApps.get(name);
            App mergedApp = new App(
                    guideApp.name(),
                    guideApp.packageName(),
                    guideApp.applicationType(),
                    guideApp.framework(),
                    mergeLists(guideApp.features(), baseApp.features()),
                    mergeLists(guideApp.invisibleFeatures(), baseApp.invisibleFeatures()),
                    mergeLists(guideApp.javaFeatures(), baseApp.javaFeatures()),
                    mergeLists(guideApp.kotlinFeatures(), baseApp.kotlinFeatures()),
                    mergeLists(guideApp.groovyFeatures(), baseApp.groovyFeatures()),
                    guideApp.testFramework(),
                    guideApp.excludeTest(),
                    guideApp.excludeSource(),
                    baseApp.validateLicense()
            );
            merged.add(mergedApp);
        }

        return merged;
    }

    /**
     * Merges multiple collections into one list.
     *
     * @param lists An array of Collection objects to be merged.
     * @return A single List containing all elements from the provided Collections, excluding any null values.
     */
    private static List mergeLists(Collection... lists) {
        List merged = new ArrayList<>();
        for (Collection list : lists) {
            if (list != null) {
                merged.addAll(list);
            }
        }
        return merged;
    }

    /**
     * Adds all elements from the source collection to the target collection safely.
     *
     * @param target The collection where elements will be added.
     * @param src The collection whose elements are to be added to the target.
     * @throws NullPointerException If the target collection is null.
     */
    private static void addAllSafe(Collection target, Collection src) {
        if(target == null) {
            throw new NullPointerException("Target list cannot be null");
        }

        if(src != null) {
            target.addAll(src);
        }
    }

    static void mergeMetadataList(List<Guide> metadatas) {
        Map<String, Guide> metadatasByDirectory = new TreeMap<>();
        for (Guide metadata : metadatas) {
            metadatasByDirectory.put(metadata.slug(), metadata);
        }

        mergeMetadataMap(metadatasByDirectory);

        metadatas.clear();
        metadatas.addAll(metadatasByDirectory.values());
    }

    private static void mergeMetadataMap(Map<String, Guide> metadatasByDirectory) {
        List<String> dirs = new ArrayList<>(metadatasByDirectory.keySet());

        for (String dir : dirs) {
            Guide metadata = metadatasByDirectory.get(dir);
            if (metadata.base() != null) {
                Guide base = metadatasByDirectory.get(metadata.base());
                Guide merged = GuideUtils.merge(base, metadata);
                metadatasByDirectory.put(dir, merged);
            }
        }
    }
}
