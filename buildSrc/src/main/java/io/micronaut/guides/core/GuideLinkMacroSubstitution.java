package io.micronaut.guides.core;

import jakarta.inject.Singleton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.micronaut.guides.core.MacroUtils.findMacroInstances;

@Singleton
public class GuideLinkMacroSubstitution implements MacroSubstitution {
    private static final Pattern GUIDE_LINK_REGEX = Pattern.compile("guideLink:(.*?)\\[(.*?)]");

    private static String processGuideLink(String line) {
        Matcher matcher = GUIDE_LINK_REGEX.matcher(line);
        if (matcher.find()) {
            String slug = matcher.group(1).trim();
            String text = matcher.group(2);
            return "link:" + slug + ".html[" + text + "]";
        }
        return line;
    }

    @Override
    public String substitute(String str, Guide guide, GuidesOption option) {
        for (String instance : findMacroInstances(str, GUIDE_LINK_REGEX)) {
            String res = processGuideLink(instance);
            str = str.replace(instance, res);
        }

        return str;
    }

}
