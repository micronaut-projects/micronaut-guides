package io.micronaut.guides.core;

public interface MacroMetadataSubstitution {
    String substitute(String str, GuidesOption option, Guide guide);
}
