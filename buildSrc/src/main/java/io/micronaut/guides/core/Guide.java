package io.micronaut.guides.core;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.jsonschema.JsonSchema;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@JsonSchema
@Serdeable
public record Guide(
        @NonNull
        @JsonPropertyDescription("The guide's title")
        String title,

        @NonNull
        @JsonPropertyDescription("The guide introduction")
        String intro,

        @NonNull
        @JsonPropertyDescription("The guide's authors")
        List<String> authors,

        @NonNull
        @JsonPropertyDescription("The guide's categories")
        List<String> categories,

        @NonNull
        @JsonPropertyDescription("The guide publication date. It should follow the format YYYY-MM-DD")
        LocalDate publicationDate,

        @JsonPropertyDescription("If the guide needs a minimum Java version, define it here")
        Integer minimumJavaVersion,

        @JsonPropertyDescription("If the guide needs a maximum Java version, define it here")
        Integer maximumJavaVersion,

        @JsonPropertyDescription("The acronym for the cloud service provider of the guide. For example, OCI for Oracle Cloud Infrastructure")
        Cloud cloud,

        @JsonPropertyDescription("Set it to true to skip running the tests for the Gradle applications for the guide")
        Boolean skipGradleTests,

        @JsonPropertyDescription("Set it to true to skip running the tests for the Maven applications for the guide")
        Boolean skipMavenTests,

        @JsonPropertyDescription("The guide asciidoc file. If not specified, the guide slug followed by the .adoc suffix is used")
        String asciidoctor,

        @JsonPropertyDescription("The guide supported languages")
        List<Language> languages,

        @JsonPropertyDescription("List of tags added to the guide. features are added automatically as tags. No need to repeat them here")
        List<String> tags,

        @JsonPropertyDescription("By default the code in the guide is generated for Gradle and Maven. If a guide is specific only for a build tool, define it here")
        List<BuildTool> buildTools,

        @JsonPropertyDescription("The guide's test framework. By default Java and Kotlin applications are tested with JUnit5 and Groovy applications with Spock")
        TestFramework testFramework,

        @JsonPropertyDescription("List of additional files with a relative path to include in the generated zip file for the guide")
        List<String> zipIncludes,

        @JsonPropertyDescription("The guide's slug. If not specified, the guides folder is used.")
        String slug,

        @JsonPropertyDescription("Whether the guide should be published, it defaults to true. You can set it to false for draft or base guides.")
        Boolean publish,

        @JsonPropertyDescription("Defaults to null; if set, indicates directory name of the base guide to copy before copying the current one")
        String base,

        @JsonPropertyDescription("The guide's environment variables")
        Map<String, String> env,

        @JsonPropertyDescription("Applications created for the guide.")
        App apps
) {
        enum Language{
                JAVA, GROOVY, KOTLIN;
        }

        enum BuildTool{
                MAVEN, GRADLE, GRADLE_KOTLIN;
        }

        enum TestFramework{
                SPOCK, JUNIT;
        }
}
