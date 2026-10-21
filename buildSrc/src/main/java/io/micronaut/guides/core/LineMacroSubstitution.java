package io.micronaut.guides.core;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.guides.core.asciidoc.Argument;
import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import io.micronaut.guides.core.asciidoc.Attribute;
import io.micronaut.guides.core.asciidoc.IncludeDirective;
import io.micronaut.starter.options.BuildTool;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static io.micronaut.guides.core.MacroUtils.findMacroLines;

abstract class LineMacroSubstitution implements MacroSubstitution {
    private static final String ADOC_EXTENSION = ".adoc";
    private static final String PYRONAUT_SUFFIX = "-pyronaut";

    private final Path directory;

    protected LineMacroSubstitution() {
        this(null);
    }

    /**
     * @param directory The real directory that the include attribute returned by {@link #getBaseDirectory()} points to.
     *                  When set, Pyronaut options include the {@code -pyronaut} variant of a file if it exists in it.
     */
    protected LineMacroSubstitution(@Nullable Path directory) {
        this.directory = directory;
    }

    protected abstract String getMacroName();

    protected abstract String getBaseDirectory();

    protected abstract String getPrefix();

    @Override
    public String substitute(String str, Guide guide, GuidesOption option) {
        for (String line : findMacroLines(str, getMacroName())) {
            Optional<AsciidocMacro> asciidocMacroOptional = AsciidocMacro.of(getMacroName(), line);
            if (asciidocMacroOptional.isEmpty()) {
                continue;
            }

            AsciidocMacro macro = asciidocMacroOptional.get();
            StringBuilder builder = new StringBuilder();

            for (Attribute attribute : macro.attributes()) {
                Argument argument = new Argument(attribute.key(), attribute.values().get(0));
                builder.append(argument).append("\n");
            }

            Path target = Path.of(getBaseDirectory(), getPrefix() + macro.target());
            if (directory != null && option != null && option.getBuildTool() == BuildTool.PYRONAUT) {
                target = pyronautTarget(macro.target()).orElse(target);
            }

            IncludeDirective.Builder includeDirectiveBuilder = IncludeDirective.builder().target(target.toString());
            builder.append(includeDirectiveBuilder.build());

            str = str.replace(line, builder.toString());
        }
        return str;
    }

    /**
     * @return the include target of the Pyronaut-specific variant (<code>&lt;prefix&gt;&lt;name&gt;-pyronaut.adoc</code>) if that file exists.
     */
    private Optional<Path> pyronautTarget(String name) {
        String baseName = name.endsWith(ADOC_EXTENSION) ? name.substring(0, name.length() - ADOC_EXTENSION.length()) : name;
        String fileName = getPrefix() + baseName + PYRONAUT_SUFFIX + ADOC_EXTENSION;
        return Files.isRegularFile(directory.resolve(fileName))
                ? Optional.of(Path.of(getBaseDirectory(), fileName))
                : Optional.empty();
    }
}
