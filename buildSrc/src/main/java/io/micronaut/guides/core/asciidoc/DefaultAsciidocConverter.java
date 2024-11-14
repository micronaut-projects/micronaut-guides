package io.micronaut.guides.core.asciidoc;

import jakarta.inject.Singleton;
import org.asciidoctor.*;

import java.io.File;

@Singleton
public class DefaultAsciidocConverter implements AsciidocConverter {

    OptionsBuilder optionsBuilder;

    Asciidoctor asciidoctor;

    DefaultAsciidocConverter(AsciidocConfiguration asciidocConfiguration) {
        Attributes attributes = Attributes.builder()
                .attribute("sourcedir", asciidocConfiguration.getSourceDir())
                .attribute("commonsDir", asciidocConfiguration.getCommonsDir())
                .attribute("guidesDir", asciidocConfiguration.getGuidesDir())
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

        optionsBuilder = Options.builder()
                .docType(asciidocConfiguration.getDocType())
                .eruby(asciidocConfiguration.getRuby())
                .templateDirs(asciidocConfiguration.getTemplateDirs())
                .attributes(attributes)
                .safe(SafeMode.UNSAFE)
                .baseDir(new File(asciidocConfiguration.getBaseDir()));

        asciidoctor = Asciidoctor.Factory.create();
    }

    @Override
    public void convert(File source, File destination) {
        asciidoctor.convertFile(source, optionsBuilder.toFile(destination).build());
    }

    @Override
    public String convert(File source) {
        return asciidoctor.convertFile(source, optionsBuilder.toFile(false).build());
    }
}
