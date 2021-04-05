package io.micronaut.guides

import groovy.transform.CompileStatic
import io.micronaut.starter.application.ApplicationType

@CompileStatic
class GuideMetadata {

    String asciidoctor
    String slug
    String title
    String intro
    List<String> authors

    List<String> buildTools
    List<String> languages
    String testFramework

    boolean skipGradleTests
    boolean skipMavenTests

    List<App> apps

    static class App {
        ApplicationType applicationType
        String name
        List<String> features
    }
}
