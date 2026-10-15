package io.micronaut.guides.core.asciidoc;

import io.micronaut.core.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SourceBlock {
    private static final String SEPARATOR = "----";

    @Nullable
    private final String title;

    @Nullable
    private final List<IncludeDirective> includeDirectives;

    @Nullable
    private final String language;

    public SourceBlock(String title,
                       List<IncludeDirective> includeDirectives,
                       String language) {
        this.title = title;
        this.includeDirectives = includeDirectives;
        this.language = language;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public List<IncludeDirective> getIncludeDirectives() {
        return includeDirectives;
    }

    @Nullable
    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        List<String> lines = new ArrayList<>();
        lines.add("[source," + getLanguage() + "]");
        if (getTitle() != null) {
            lines.add("." + getTitle());
        }
        lines.add(SEPARATOR);
        getIncludeDirectives().stream()
                .map(IncludeDirective::toString)
                .forEach(lines::add);
        lines.add(SEPARATOR);
        return String.join("\n", lines);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        @Nullable
        private String title;

        @Nullable
        private List<IncludeDirective> includeDirectives;

        @Nullable
        private String language;

        public Builder title(@Nullable String title) {
            this.title = title;
            return this;
        }

        public Builder includeDirectives(@Nullable List<IncludeDirective> includeDirectives) {
            this.includeDirectives = includeDirectives;
            return this;
        }

        public Builder includeDirective(@Nullable IncludeDirective includeDirective) {
            if (this.includeDirectives == null) {
                this.includeDirectives = new ArrayList<>();
            }
            this.includeDirectives.add(includeDirective);
            return this;
        }

        public Builder language(@Nullable String language) {
            this.language = language;
            return this;
        }

        public SourceBlock build() {
            return new SourceBlock(title, includeDirectives, language);
        }
    }
}
