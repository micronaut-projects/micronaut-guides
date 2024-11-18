package io.micronaut.guides.core;

import io.micronaut.guides.core.asciidoc.Argument;
import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import io.micronaut.guides.core.asciidoc.Attribute;
import io.micronaut.guides.core.asciidoc.IncludeDirective;

import java.nio.file.Path;
import java.util.Optional;

import static io.micronaut.guides.core.MacroUtils.findMacroLines;

abstract class LineMacroSubstitution implements MacroSubstitution {
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

            IncludeDirective.Builder includeDirectiveBuilder = IncludeDirective.builder().target(target.toString());
            builder.append(includeDirectiveBuilder.build());

            str = str.replace(line, builder.toString());
        }
        return str;
    }
}
