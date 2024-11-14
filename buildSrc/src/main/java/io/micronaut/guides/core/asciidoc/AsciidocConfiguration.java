package io.micronaut.guides.core.asciidoc;

import org.asciidoctor.Placement;

import java.io.File;

interface AsciidocConfiguration {
    String getSourceDir();

    String getSourceHighlighter();

    Placement getToc();

    int getToclevels();

    boolean getSectnums();

    String getIdprefix();

    String getIdseparator();

    String getIcons();

    String getImagesdir();

    boolean isNofooter();

    String getDocType();

    String getRuby();

    File getTemplateDirs();

    String getCommonsDir();

    String getBaseDir();

    String getGuidesDir();
}
