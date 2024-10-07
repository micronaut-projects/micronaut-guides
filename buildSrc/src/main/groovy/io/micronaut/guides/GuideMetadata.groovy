package io.micronaut.guides

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.ToString
import io.micronaut.core.annotation.Nullable
import io.micronaut.starter.api.TestFramework
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

import java.time.LocalDate

@ToString(includeNames = true)
@CompileStatic
class GuideMetadata {
    private static final String MICRONAUT_PREFIX = "micronaut-"
    private static final String VIEWS_PREFIX = "views-"
    private static final List<String> FEATURES_PREFIXES = List.of(MICRONAUT_PREFIX, VIEWS_PREFIX)
    String asciidoctor
    String slug
    String title
    String intro
    Set<String> authors
    List<String> tags

    @Nullable
    Cloud cloud
    List<Category> categories
    LocalDate publicationDate

    boolean publish
    String base

    List<String> buildTools
    List<String> languages
    String testFramework

    boolean skipGradleTests
    boolean skipMavenTests

    Integer minimumJavaVersion
    Integer maximumJavaVersion

    List<String> zipIncludes

    Set<Skip> skips

    Map<String, String> env

    List<App> apps

    Set<String> getFrameworks() {
        apps.collect { it.framework }.unique() as Set<String>
    }

    List<String> getTags() {

        Set<String> tagsList = new HashSet<>()
        if (this.tags != null) {
            tagsList.addAll(this.tags)
        }
        for (App app : this.apps) {
            for (String featureName : app.javaFeatures + app.kotlinFeatures + app.groovyFeatures + app.visibleFeatures) {
                String tagToAdd = featureName
                for (String prefix : FEATURES_PREFIXES) {
                    if (tagToAdd.startsWith(prefix)) {
                        tagToAdd = tagToAdd.substring(prefix.length())
                    }
                }
                tagsList.add(tagToAdd)
            }
        }
        Set<String> categoriesAsTags = this.categories.collect { cat -> cat.name().toLowerCase() } as Set
        tagsList.addAll(categoriesAsTags)
        tagsList as List<String>
    }

    boolean shouldSkip(BuildTool buildTool) {
        if (buildTool == BuildTool.GRADLE) {
            return skipGradleTests
        }
        if (buildTool == BuildTool.MAVEN) {
            return skipMavenTests
        }
        false
    }

    boolean shouldSkip(BuildTool buildTool, Language language) {
        return skips.contains(new Skip(buildTool, language))
    }

    @Canonical
    @CompileStatic
    static class Skip {

        final BuildTool buildTool
        final Language language

        Skip(String buildTool, String language) {
            this.buildTool = BuildTool.valueOf(buildTool.toUpperCase())
            this.language = Language.valueOf(language.toUpperCase())
        }

        Skip(BuildTool buildTool, Language language) {
            this.buildTool = buildTool
            this.language = language
        }
    }

    @ToString(includeNames = true)
    @CompileStatic
    static class App {
        boolean validateLicense = true
        private static final String FEATURE_SPOTLESS = "spotless"
        String framework
        TestFramework testFramework
        ApplicationType applicationType
        String name
        List<String> javaFeatures
        List<String> kotlinFeatures
        List<String> groovyFeatures
        List<String> visibleFeatures
        List<String> invisibleFeatures
        List<String> excludeSource
        List<String> excludeTest

        List<String> getFeatures(Language language) {
            if (language == Language.JAVA) {
                return visibleFeatures + getInvisibleFeatures() + javaFeatures
            }
            if (language == Language.KOTLIN) {
                return visibleFeatures + getInvisibleFeatures() + kotlinFeatures
            }
            if (language == Language.GROOVY) {
                return visibleFeatures + getInvisibleFeatures() + groovyFeatures
            }
            visibleFeatures + getInvisibleFeatures()
        }

        List<String> getInvisibleFeatures() {
            if (validateLicense) {
                List<String> result = new ArrayList<>()
                if (invisibleFeatures) {
                    result.addAll(invisibleFeatures)
                }
                result.add(FEATURE_SPOTLESS)
                return result
            }
            return invisibleFeatures
        }

        List<String> getVisibleFeatures(Language language) {
            if (language == Language.JAVA) {
                return visibleFeatures + javaFeatures
            }
            if (language == Language.KOTLIN) {
                return visibleFeatures + kotlinFeatures
            }
            if (language == Language.GROOVY) {
                return visibleFeatures + groovyFeatures
            }
            visibleFeatures
        }
    }

}
