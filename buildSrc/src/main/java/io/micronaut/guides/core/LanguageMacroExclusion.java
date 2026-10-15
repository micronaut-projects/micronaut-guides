package io.micronaut.guides.core;

import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class LanguageMacroExclusion extends MacroExclusion {
    private static final String MACRO_LANGUAGE_EXCLUSION = "exclude-for-languages";

    @Override
    protected String getMacroName() {
        return MACRO_LANGUAGE_EXCLUSION;
    }

    @Override
    protected boolean shouldExclude(List<String> params, GuidesOption option, Guide guide) {
        return params.contains(option.getLanguage().toString());
    }
}
