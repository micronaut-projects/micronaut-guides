package io.micronaut.guides.core;

public interface MacroSubstitution {
    String substitute(String str, String slug, GuidesOption option);
}
