package io.micronaut.guides.core.asciidoc;

import io.micronaut.guides.core.VersionLoader;
import jakarta.inject.Singleton;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class DefaultAsciidocConverter implements AsciidocConverter {

    Map<String, Object> config = new HashMap<>();


    DefaultAsciidocConverter(VersionLoader versionLoader) {
        config.put("sourcedir", "build/code");
        config.put("source-highlighter", "coderay");
        config.put("toc", "left");
        config.put("toclevels", 2);
        config.put("sectnums", "");
        config.put("idprefix", "");
        config.put("idseparator", "-");
        config.put("icons", "font");
        config.put("imagesdir", "images");
        config.put("nofooter", true);
        config.put("project-version", versionLoader.getVersion());
    }

    @Override
    public String convert(File source) {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        Options options = new Options();
        options.setAttributes(config);
        asciidoctor.convertFile(source, options);
        return null;
    }
}
