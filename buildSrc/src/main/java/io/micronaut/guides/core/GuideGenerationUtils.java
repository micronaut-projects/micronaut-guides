package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import jakarta.validation.constraints.NotNull;
import org.gradle.api.GradleException;
import org.gradle.api.JavaVersion;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static io.micronaut.starter.api.TestFramework.JUNIT;
import static io.micronaut.starter.api.TestFramework.SPOCK;
import static io.micronaut.starter.options.Language.GROOVY;

public class GuideGenerationUtils {


    private GuideGenerationUtils() {
    }

    @NonNull
    static String mainPath(@NonNull String appName,
                           @NonNull String fileName,
                           @NonNull GuidesOption option,
                           @NonNull GuidesConfiguration configuration) {
        return pathByFolder(appName, fileName, "main", option, configuration);
    }

    @NonNull
    static String testPath(@NonNull String appName,
                           @NonNull String name,
                           @NonNull GuidesOption option,
                           @NonNull GuidesConfiguration configuration) {
        String fileName = name;
        if (option.getTestFramework() != null) {
            if (name.endsWith("Test")) {
                fileName = name.substring(0, name.indexOf("Test"));
                fileName += option.getTestFramework() == SPOCK ? "Spec" : "Test";
            }
        }

        return pathByFolder(appName, fileName, "test", option, configuration);
    }

    @NonNull
    static String pathByFolder(@NonNull String appName,
                               @NonNull String fileName,
                               @NonNull String folder,
                               @NonNull GuidesOption option,
                               @NonNull GuidesConfiguration configuration) {
        String module = StringUtils.isNotEmpty(appName) ? appName + "/" : "";
        Path path = Path.of(module,
                "src",
                folder,
                option.getLanguage().getName(),
                configuration.getPackageName().replace(".", "/"),
                fileName + "." + option.getLanguage().getExtension());
        return path.toString();
    }

    @NonNull
    static List<GuidesOption> guidesOptions(@NonNull Guide guideMetadata,
                                            @NonNull Logger logger) {
        List<BuildTool> buildTools = guideMetadata.buildTools();
        List<Language> languages = guideMetadata.languages();
        TestFramework testFramework = guideMetadata.testFramework();
        List<GuidesOption> guidesOptionList = new ArrayList<>();

        for (BuildTool buildTool : buildTools) {
            for (Language language : Language.values()) {
                if (GuideUtils.shouldSkip(guideMetadata, buildTool)) {
                    logger.info("Skipping index guide for {} and {}", buildTool, language);
                    continue;
                }
                if (languages.contains(language)) {
                    guidesOptionList.add(new GuidesOption(buildTool, language, testFrameworkOption(language, testFramework)));
                }
            }
        }

        return guidesOptionList;
    }

    @NonNull
    static TestFramework testFrameworkOption(@NonNull Language language,
                                             @Nullable TestFramework testFramework) {
        if (testFramework != null) {
            return testFramework;
        }
        if (language == GROOVY) {
            return SPOCK;
        }
        return JUNIT;
    }

    @NonNull
    static JdkVersion resolveJdkVersion(@NonNull GuidesConfiguration guidesConfiguration) {
        JdkVersion javaVersion;
        if (System.getenv(guidesConfiguration.getEnvJdkVersion()) != null) {
            try {
                int majorVersion = Integer.parseInt(System.getenv(guidesConfiguration.getEnvJdkVersion()));
                javaVersion = JdkVersion.valueOf(majorVersion);
            } catch (NumberFormatException ignored) {
                throw new GradleException("Could not parse env " + guidesConfiguration.getEnvJdkVersion() + " to JdkVersion");
            }
        } else {
            try {
                javaVersion = JdkVersion.valueOf(Integer.parseInt(JavaVersion.current().getMajorVersion()));
            } catch (IllegalArgumentException ex) {
                System.out.println("WARNING: " + ex.getMessage() + ": Defaulting to " + guidesConfiguration.getDefaultJdkVersion());
                javaVersion = guidesConfiguration.getDefaultJdkVersion();
            }
        }
        return javaVersion;
    }

    public static JdkVersion resolveJdkVersion(GuidesConfiguration guidesConfiguration, @NotNull @NonNull Guide guide) {
        JdkVersion javaVersion = GuideGenerationUtils.resolveJdkVersion(guidesConfiguration);
        if (guide.minimumJavaVersion() != null) {
            JdkVersion minimumJavaVersion = JdkVersion.valueOf(guide.minimumJavaVersion());
            if (minimumJavaVersion.majorVersion() > javaVersion.majorVersion()) {
                return minimumJavaVersion;
            }
        }
        return javaVersion;
    }

    static boolean skipBecauseOfJavaVersion(Guide metadata, GuidesConfiguration guidesConfiguration) {
        int jdkVersion = resolveJdkVersion(guidesConfiguration).majorVersion();
        return (metadata.minimumJavaVersion() != null && jdkVersion < metadata.minimumJavaVersion()) ||
                (metadata.maximumJavaVersion() != null && jdkVersion > metadata.maximumJavaVersion());
    }

    public static String singleGuide(GuidesConfiguration guidesConfiguration) {
        return System.getProperty(guidesConfiguration.getSysPropMicronautGuide());
    }

    public static boolean process(Guide metadata, boolean checkJdk, GuidesConfiguration guidesConfiguration) {

        if (!metadata.publish()) {
            return false;
        }

        boolean processGuide = singleGuide(guidesConfiguration) == null || singleGuide(guidesConfiguration).equals(metadata.slug());
        if (!processGuide) {
            return false;
        }

        if (checkJdk && skipBecauseOfJavaVersion(metadata, guidesConfiguration)) {
            System.out.println("Not processing " + metadata.slug() + ", JDK not between " +
                    metadata.minimumJavaVersion() + " and " + metadata.maximumJavaVersion());
            return false;
        }

        return true;
    }

}
