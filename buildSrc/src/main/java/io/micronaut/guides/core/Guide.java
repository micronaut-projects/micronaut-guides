package io.micronaut.guides.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.jsonschema.JsonSchema;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @param title The guide's title
 * @param intro The guide introduction
 * @param authors The guide's authors
 * @param categories The guide's categories
 * @param publicationDate The guide publication date. It should follow the format YYYY-MM-DD
 * @param minimumJavaVersion If the guide needs a minimum Java version, define it here
 * @param maximumJavaVersion If the guide needs a maximum Java version, define it here
 * @param cloud The acronym for the cloud service provider of the guide. For example, OCI for Oracle Cloud Infrastructure
 * @param skipGradleTests Set it to true to skip running the tests for the Gradle applications for the guide
 * @param skipMavenTests Set it to true to skip running the tests for the Maven applications for the guide
 * @param asciidoctor The guide asciidoc file. If not specified, the guide slug followed by the .adoc suffix is used
 * @param languages The guide supported languages
 * @param tags List of tags added to the guide. features are added automatically as tags. No need to repeat them here
 * @param buildTools By default the code in the guide is generated for Gradle and Maven. If a guide is specific only for a build tool, define it here
 * @param testFramework The guide's test framework. By default Java and Kotlin applications are tested with JUnit5 and Groovy applications with Spock
 * @param zipIncludes List of additional files with a relative path to include in the generated zip file for the guide
 * @param slug The guide's slug. If not specified, the guides folder is used
 * @param publish Whether the guide should be published, it defaults to true. You can set it to false for draft or base guides
 * @param base Defaults to null; if set, indicates directory name of the base guide to copy before copying the current one
 * @param env The guide's environment variables
 * @param apps Applications created for the guide
 */
@JsonSchema
@Serdeable
public record Guide(
        @NonNull
        @NotBlank
        String title,

        @NonNull
        @NotBlank
        String intro,

        @NotEmpty
        @NonNull
        List<String> authors,

        @NotEmpty
        @NonNull
        List<String> categories,

        @NonNull
        @NotNull
        LocalDate publicationDate,

        @Nullable
        Integer minimumJavaVersion,

        @Nullable
        Integer maximumJavaVersion,

        @Nullable
        Cloud cloud,

        @JsonProperty(defaultValue = StringUtils.FALSE)
        @Nullable
        Boolean skipGradleTests,

        @JsonProperty(defaultValue = StringUtils.FALSE)
        @Nullable
        Boolean skipMavenTests,

        @Nullable
        String asciidoctor,

        @Nullable
        List<Language> languages,

        @Nullable
        List<String> tags,

        @Nullable
        List<BuildTool> buildTools,

        @Nullable
        TestFramework testFramework,

        @Nullable
        List<String> zipIncludes,

        @Nullable
        String slug,

        @Nullable
        @JsonProperty(defaultValue = StringUtils.TRUE)
        Boolean publish,

        @Nullable
        String base,

        @Nullable
        Map<String, String> env,

        @NotEmpty
        @NonNull
        List<App> apps
) {
}
