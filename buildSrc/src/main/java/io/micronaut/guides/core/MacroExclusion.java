package io.micronaut.guides.core;

import java.util.List;

abstract class MacroExclusion implements MacroSubstitution {
    private static final String LINE_BREAK = "\n";

    protected abstract String getMacroName();

    protected abstract boolean shouldExclude(List<String> params, GuidesOption option, Guide guide);

    @Override
    public String substitute(String str, Guide guide, GuidesOption option) {
        for (List<String> group : MacroUtils.findMacroGroupsNested(str, getMacroName())) {
            List<String> params = MacroUtils.extractMacroGroupParameters(group.get(0), getMacroName());
            if (shouldExclude(params, option, guide)) {
                str = str.replace(String.join(LINE_BREAK, group)+ LINE_BREAK, "");
            } else{
                str = str.replace(String.join(LINE_BREAK, group), String.join(LINE_BREAK, group.subList(1, group.size()-1)));
            }
        }
        return str;
    }
}
