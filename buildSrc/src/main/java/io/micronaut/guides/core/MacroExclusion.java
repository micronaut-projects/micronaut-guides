package io.micronaut.guides.core;

import java.util.List;

abstract class MacroExclusion implements MacroMetadataSubstitution {

    protected abstract String getMacroName();

    protected abstract boolean shouldExclude(List<String> params, GuidesOption option, Guide guide);

    @Override
    public String substitute(String str, GuidesOption option, Guide guide) {
        for (List<String> group : MacroUtils.findMacroGroupsNested(str, getMacroName())) {
            List<String> params = MacroUtils.extractMacroGroupParameters(group.get(0), getMacroName());
            if (shouldExclude(params, option, guide)) {
                str = str.replace(String.join("\n", group)+"\n", "");
            } else{
                str = str.replace(String.join("\n", group), String.join("\n", group.subList(1, group.size()-1)));
            }
        }
        return str;
    }
}
