package io.micronaut.guides.core;

import io.micronaut.guides.core.asciidoc.*;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Optional;

import static io.micronaut.guides.core.MacroUtils.*;
import static io.micronaut.guides.core.asciidoc.IncludeDirective.ATTRIBUTE_LINES;

abstract class SourceBlockMacroSubstitution implements MacroSubstitution {

    private final LicenseLoader licenseLoader;
    private final GuidesConfiguration guidesConfiguration;
    
    SourceBlockMacroSubstitution(LicenseLoader licenseLoader,
                                 GuidesConfiguration guidesConfiguration) {
        this.licenseLoader = licenseLoader;
        this.guidesConfiguration = guidesConfiguration;
    }

    protected abstract String getMacroName();

    protected abstract Classpath getClasspath();

    protected abstract FileType getFileType();

    public LicenseLoader getLicenseLoader() {
        return licenseLoader;
    }

    public GuidesConfiguration getGuidesConfiguration() {
        return guidesConfiguration;
    }

    @Override
    public String substitute(String str, String slug, GuidesOption option) {
        for (String line : findMacroLines(str, getMacroName())) {
            Optional<AsciidocMacro> asciidocMacroOptional = AsciidocMacro.of(getMacroName(), line);
            if (asciidocMacroOptional.isPresent()) {
                AsciidocMacro asciidocMacro = asciidocMacroOptional.get();
                String appName = asciidocMacro.attributes().stream()
                        .filter(attribute -> attribute.key().equals(APP))
                        .map(Attribute::values)
                        .filter(l -> !l.isEmpty())
                        .map(l -> l.get(0))
                        .findFirst()
                        .orElse(APP);

                String condensedTarget = condensedTarget(asciidocMacro, option);
                String[] arr;
                int lastIndex = condensedTarget.lastIndexOf('.');
                if (lastIndex != -1 && lastIndex != condensedTarget.length() - 1) {
                    String prefix = condensedTarget.substring(0, lastIndex);
                    String extension = condensedTarget.substring(lastIndex + 1);
                    arr = new String[]{prefix, extension};
                } else {
                    arr = new String[]{condensedTarget};
                }
                String language = getLanguage(option);
                String extension = getExtension(option);

                if (arr.length == 2) {
                    language = arr[arr.length-1];
                    language = resolveAsciidoctorLanguage(language);
                } else {
                    condensedTarget = condensedTarget + "." + extension;
                }

                String target = sourceInclude(slug, appName, condensedTarget, getClasspath(), option, language, getGuidesConfiguration().getPackageName());
                String title = Path.of(target).normalize().toString().replace("{sourceDir}/"+slug+"/", "").replace(getSourceDir(slug, option)+"/", "");

                IncludeDirective.Builder includeDirectiveBuilder = IncludeDirective.builder().attributes(asciidocMacro.attributes())
                        .target(target);
                if (getFileType() == FileType.CODE) {
                    Range range = new Range(getLicenseLoader().getNumberOfLines(), -1);
                    if (range.isValid() && asciidocMacro.attributes().stream().noneMatch(attribute -> attribute.key().equals(ATTRIBUTE_LINES))) {
                        includeDirectiveBuilder.lines(range);
                    }
                }
                String replacement = SourceBlock.builder()
                        .title(title)
                        .language(language)
                        .includeDirective(includeDirectiveBuilder.build())
                        .build()
                        .toString();
                str = str.replace(line, replacement);
            }
        }
        return str;
    }

    protected String getLanguage(GuidesOption option){
        return option.getLanguage().toString();
    }

    protected String getExtension(GuidesOption option){
        return option.getLanguage().getExtension();
    }

    protected String sourceTitle(
            String appName,
            String condensedTarget,
            Classpath classpath,
            String language,
            String packageName) {
        return (appName.equals(MacroSubstitution.APP) ? "" : (appName + "/")) + sourceConventionFolder(classpath, language) + "/"
                + (getFileType() == FileType.CODE ? (packageName.replace(".", "/") + "/") : "")
                + condensedTarget;
    }

    protected String sourceInclude(
            String slug,
            String appName,
            String condensedTarget,
            Classpath classpath,
            GuidesOption option,
            String language,
            String packageName) {
        return "{sourceDir}/" + slug + "/" + getSourceDir(slug,option) + "/" +
                sourceTitle(appName, condensedTarget, classpath, language, packageName);
    }

    private String sourceConventionFolder(Classpath classpath, String language) {
        if (getFileType() == FileType.CODE) {
            return "src/" + classpath + "/" + language;
        } else if (getFileType() == FileType.RESOURCE) {
            return "src/" + classpath + "/resources";
        }
        throw new UnsupportedOperationException("Unimplemented sourceConventionFolder for " + getFileType());
    }

    protected String condensedTarget(@NotNull AsciidocMacro asciidocMacro, GuidesOption option) {
        return asciidocMacro.target();
    }
}