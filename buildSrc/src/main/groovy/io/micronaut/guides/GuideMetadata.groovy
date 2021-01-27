package io.micronaut.guides

import groovy.transform.CompileStatic

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
        String name
        List<String> features
    }
}
