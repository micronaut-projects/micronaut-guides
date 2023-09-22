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
        if (this.tags == null && this.categories == null) {
            return Collections.emptyList()
        }
        if (this.categories == null) {
            return this.tags
        }
        Set<String> categoriesAsTags = this.categories.stream().map(cat -> cat.name().toLowerCase()).collect(Collectors.toSet());
        if (this.tags == null) {
            return categoriesAsTags as List<String>
        }
         Set<String> result = new HashSet<>()
         result.addAll(this.tags)
         result.addAll(categoriesAsTags)
         result as List<String>
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
