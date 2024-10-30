package io.micronaut.guides.core;

import io.micronaut.core.util.StringUtils;
import io.micronaut.guides.core.asciidoc.PlacheholderMacro;
import java.util.Optional;

abstract class PlaceholderWithTargetMacroSubstitution implements MacroSubstitution {

    protected abstract String getMacroName();

    protected abstract String getSostitution(Guide guide, GuidesOption option, String app);

    public String substitute(String str, Guide guide, GuidesOption option) {
        for (String instance : MacroUtils.findMacroInstances(str, getMacroName())) {
            Optional<PlacheholderMacro> macroOptional = PlacheholderMacro.of(getMacroName(), instance);
            if (macroOptional.isEmpty()) {
                continue;
            }
            PlacheholderMacro macro = macroOptional.get();
            String app = StringUtils.isNotEmpty(macro.target()) ? macro.target() : "default";
            String res = getSostitution(guide, option, app);
            str = str.replace(instance, res);
        }
        return str;
    }
}
