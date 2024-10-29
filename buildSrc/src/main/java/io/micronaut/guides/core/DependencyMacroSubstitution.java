package io.micronaut.guides.core;

import jakarta.inject.Singleton;
import java.util.List;

import static io.micronaut.guides.core.MacroUtils.*;

@Singleton
public class DependencyMacroSubstitution implements MacroSubstitution{
    @Override
    public String substitute(String str, Guide guide, GuidesOption option) {
        for(String block : findMacroGroups(str, "dependencies")){
            List<String> lines = DependencyLines.asciidoc(block.replace(":dependencies:","").strip().lines().toList(), option.getBuildTool(), option.getLanguage());
            str = str.replace(block,String.join("\n", lines));
        }
        for(String line : findMacroLines(str, "dependency")){
            List<String> lines = DependencyLines.asciidoc(line, option.getBuildTool(), option.getLanguage());
            str = str.replace(line,String.join("\n", lines));
        }
        return str;
    }
}
