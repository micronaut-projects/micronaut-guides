package io.micronaut.guides.core;

public class BuildMacroExclusion implements MacroMetadataSubstitution{
    @Override
    public String substitute(String str, GuidesOption option, Guide guide) {
        return "";
    }
}
