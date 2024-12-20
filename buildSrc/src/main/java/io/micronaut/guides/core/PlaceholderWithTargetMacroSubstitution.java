package io.micronaut.guides.core;

import io.micronaut.core.util.StringUtils;
import io.micronaut.guides.core.asciidoc.PlacheholderMacro;

import java.util.Optional;
import java.util.regex.Pattern;

abstract class PlaceholderWithTargetMacroSubstitution implements MacroSubstitution {

    protected abstract String getMacroName();

    protected abstract String getSubstitution(Guide guide, GuidesOption option, String app);

    public String substitute(String str, Guide guide, GuidesOption option) {
        Pattern pattern = Pattern.compile("@(?:([\\w-]*):)?" + getMacroName() + "@");
        for (String instance : MacroUtils.findMacroInstances(str, pattern)) {
            Optional<PlacheholderMacro> macroOptional = PlacheholderMacro.of(getMacroName(), instance);
            if (macroOptional.isEmpty()) {
                continue;
            }
            PlacheholderMacro macro = macroOptional.get();
            String app = StringUtils.isNotEmpty(macro.target()) ? macro.target() : "default";
            String res = getSubstitution(guide, option, app);
            str = str.replace(instance, res);
        }
        return str;
    }
}
