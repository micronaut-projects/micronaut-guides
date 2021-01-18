package io.micronaut.guides

import groovy.transform.CompileStatic

@CompileStatic
class GuideMetadata {
    List<String> buildTools
    List<String> languages
    String testFramework

    String asciidoctor
    String slug
    String title
    String intro
    List<String> features
    List<String> authors
}
