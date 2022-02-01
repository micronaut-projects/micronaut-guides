package io.micronaut.guides

import groovy.transform.CompileStatic
import groovy.transform.ToString
import io.micronaut.starter.application.ApplicationType

import java.time.LocalDate

@ToString(includeNames = true)
@CompileStatic
class GuideMetadata {

    String asciidoctor
    String slug
    String title
    String intro
    List<String> authors
    List<String> tags
    Category category
    LocalDate publicationDate

    List<String> buildTools
    List<String> languages
    String testFramework

    boolean skipGradleTests
    boolean skipMavenTests

    Integer minimumJavaVersion
    Integer maximumJavaVersion

    List<String> zipIncludes

    List<App> apps

    @ToString(includeNames = true)
    @CompileStatic
    static class App {
        ApplicationType applicationType
        String name
        List<String> features
        List<String> excludeSource
        List<String> excludeTest
    }
}
