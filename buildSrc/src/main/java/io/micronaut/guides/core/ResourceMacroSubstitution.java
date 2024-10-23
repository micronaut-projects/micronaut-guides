package io.micronaut.guides.core;

import jakarta.inject.Singleton;

import java.util.List;

import static io.micronaut.guides.core.MacroUtils.*;

@Singleton
public class ResourceMacroSubstitution implements MacroSubstitution{
    @Override
    public String substitute(String str, String slug, GuidesOption option) {
        String resourceDir = "main";

        List<String> lines = addIncludesResources(str,slug,option,resourceDir,"resource");

        return String.join("\n", lines);
    }
}
