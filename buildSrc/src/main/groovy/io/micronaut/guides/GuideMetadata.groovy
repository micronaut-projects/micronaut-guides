package io.micronaut.guides

import groovy.transform.CompileStatic
import groovy.transform.ToString
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.api.TestFramework
import io.micronaut.starter.options.Language
import java.time.LocalDate
import java.util.stream.Collectors

@ToString(includeNames = true)
@CompileStatic
class GuideMetadata {
    private static final String MICRONAUT_PREFIX = "micronaut-"
    String asciidoctor
    String slug
    String title
    String intro
    Set<String> authors
    List<String> tags
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

    List<App> apps


    List<String> getTags() {

        Set<String> tagsList = new HashSet<>()
        if (this.tags != null) {
            tagsList.addAll(this.tags)
        }
        for (App app : this.apps) {
            for (String featureName : app.javaFeatures + app.kotlinFeatures + app.groovyFeatures + app.visibleFeatures + app.invisibleFeatures) {
                if (featureName.startsWith(MICRONAUT_PREFIX)) {
                    tagsList.add(featureName.substring(MICRONAUT_PREFIX.length()))
                } else {
                    tagsList.add(featureName)
                }
            }
        }
        Set<String> categoriesAsTags = this.categories.collect { cat -> cat.name().toLowerCase() } as Set
        tagsList.addAll(categoriesAsTags)
        tagsList as List<String>
    }

    @ToString(includeNames = true)
    @CompileStatic
    static class App {
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
                return visibleFeatures + invisibleFeatures + javaFeatures
            }
            if (language == Language.KOTLIN) {
                return visibleFeatures + invisibleFeatures + kotlinFeatures
            }
            if (language == Language.GROOVY) {
                return visibleFeatures + invisibleFeatures + groovyFeatures
            }
            visibleFeatures + invisibleFeatures
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
