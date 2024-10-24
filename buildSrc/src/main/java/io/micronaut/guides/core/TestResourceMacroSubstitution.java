package io.micronaut.guides.core;

import jakarta.inject.Singleton;

import java.util.List;

import static io.micronaut.guides.core.MacroUtils.addIncludesResources;

@Singleton
public class TestResourceMacroSubstitution implements MacroSubstitution{
    @Override
    public String substitute(String str, String slug, GuidesOption option) {
        String resourceDir = "test";

        List<String> lines = addIncludesResources(str,slug,option,resourceDir,"testResource");

        return String.join("\n", lines);
    }
}
