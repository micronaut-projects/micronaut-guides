package io.micronaut.guides.core.asciidoc;

import jakarta.inject.Singleton;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;

import java.io.File;

@Singleton
public class DefaultAsciidocConverter implements AsciidocConverter {

    Options options;

    DefaultAsciidocConverter(AsciidocConfiguration asciidocConfiguration) {
        Attributes attributes = Attributes.builder()
                .attribute("sourcedir", asciidocConfiguration.getSourceDir())
                .sourceHighlighter(asciidocConfiguration.getSourceHighlighter())
                .tableOfContents(asciidocConfiguration.getToc())
                .attribute("toclevels", asciidocConfiguration.getToclevels())
                .sectionNumbers(asciidocConfiguration.getSectnums())
                .attribute("idprefix", asciidocConfiguration.getIdprefix())
                .attribute("idseparator", asciidocConfiguration.getIdseparator())
                .icons(asciidocConfiguration.getIcons())
                .imagesDir(asciidocConfiguration.getImagesdir())
                .noFooter(asciidocConfiguration.isNofooter())
                .build();

        options = Options.builder()
                .docType(asciidocConfiguration.getDocType())
                .eruby(asciidocConfiguration.getRuby())
                .templateDirs(asciidocConfiguration.getTemplateDirs())
                .attributes(attributes)
                .safe(SafeMode.UNSAFE)
                .build();
    }

    @Override
    public String convert(File source) {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        return asciidoctor.convertFile(source, options);
    }
}
