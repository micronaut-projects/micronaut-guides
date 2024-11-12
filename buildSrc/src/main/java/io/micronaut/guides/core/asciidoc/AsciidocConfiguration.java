package io.micronaut.guides.core.asciidoc;

interface AsciidocConfiguration {
    String getSourceDir();

    String getSourceHighlighter();

    String getToc();

    int getToclevels();

    String getSectnums();

    String getIdprefix();

    String getIdseparator();

    String getIcons();

    String getImagesdir();

    boolean isNofooter();

    String getProjectVersion();

    String getDocType();

    String getRuby();

}
