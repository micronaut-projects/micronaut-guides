/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.guides.core.asciidoc;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Asciidoc Include Directive.
 * <a href="https://asciidoctor.org/docs/user-manual/#include-directive">Include Directive</a>
 */
public class IncludeDirective {

    /**
     * Target may be an absolute path, a path relative to the current document, or a URL.
     */
    @NonNull
    private final String target;

    /**
     * @see <a href="https://docs.asciidoctor.org/asciidoc/latest/directives/include-with-leveloffset/">Level Offset</a>
     */
    @Nullable
    private final String levelOffset;

    /**
     * <a href="https://docs.asciidoctor.org/asciidoc/latest/directives/include-lines/">Include Content by Line Ranges</a>
     * ranges of line numbers.
     */
    @Nullable
    private final Range lines;

    @Nullable
    private final String encoding;

    /**
     * <a href="https://docs.asciidoctor.org/asciidoc/latest/directives/include-tagged-regions/">Included Tagged Regions</a>
     */
    @Nullable
    private final List<String> tags;

    /**
     * <a href="https://docs.asciidoctor.org/asciidoc/latest/directives/include-with-indent/">Include with Ident</a>
     */
    @Nullable
    private final Integer indent;

    @Nullable
    private final String opts;

    IncludeDirective(@NonNull String target,
                            @Nullable String levelOffset,
                            @Nullable Range lines,
                            @Nullable String encoding,
                            @Nullable List<String> tags,
                            @Nullable Integer indent,
                            @Nullable String opts) {
        this.target = target;
        this.levelOffset = levelOffset;
        this.lines = lines;
        this.encoding = encoding;
        this.tags = tags;
        this.indent = indent;
        this.opts = opts;
    }

    /**
     *
     * @return Target may be an absolute path, a path relative to the current document, or a URL.
     */
    @NonNull
    public String getTarget() {
        return target;
    }

    /**
     * @see <a href="https://docs.asciidoctor.org/asciidoc/latest/directives/include-with-leveloffset/">Level Offset</a>
     */
    public @Nullable String getLevelOffset() {
        return levelOffset;
    }

    /**
     *
     * @return ranges of line numbers.
     */
    @Nullable
    public Range getLines() {
        return lines;
    }

    @Nullable
    public String getEncoding() {
        return encoding;
    }

    @Nullable
    public List<String> getTags() {
        return tags;
    }

    @Nullable
    public Integer getIndent() {
        return indent;
    }

    @Nullable
    public String getOpts() {
        return opts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append("include::")
                .append(getTarget())
                .append('[');
        List<String> attributes = attributes();
        if (CollectionUtils.isNotEmpty(attributes)) {
            sb.append(String.join(",", attributes));
        }
        sb.append("]");
        return sb.toString();
    }

    private List<String> attributes() {
        List<String> attributes = new ArrayList<>();
        if (getLevelOffset() != null) {
            attributes.add("leveloffset=" + getLevelOffset());
        }
        if (getLines() != null) {
            attributes.add("lines=" + getLines().from() + ".." + getLines().to());
        }
        if (getEncoding() != null) {
            attributes.add("encoding=" + getEncoding());
        }
        if (CollectionUtils.isNotEmpty(getTags())) {
            if (getTags().size() > 1) {
                attributes.add("tags=" + String.join(";",  getTags()));
            } else if (getTags().size() == 1) {
                attributes.add("tag=" + getTags().getFirst());
            }
        }
        if (getIndent() != null) {
            attributes.add("indent=" + getIndent());
        }
        if (getOpts() != null) {
            attributes.add("opts=" + getOpts());
        }
        return attributes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        @NonNull
        private String target;

        @Nullable
        private String levelOffset;

        @Nullable
        private Range lines;

        @Nullable
        private String encoding;

        @Nullable
        private List<String> tags;

        @Nullable
        private Integer indent;

        @Nullable
        private String opts;

        public Builder target(@NonNull String target) {
            this.target = target;
            return this;
        }

        public Builder levelOffset(@Nullable String levelOffset) {
            this.levelOffset = levelOffset;
            return this;
        }

        public Builder lines(@Nullable Range lines) {
            this.lines = lines;
            return this;
        }

        public Builder encoding(@Nullable String encoding) {
            this.encoding = encoding;
            return this;
        }

        public Builder tags(@Nullable List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Builder indent(@Nullable Integer indent) {
            this.indent = indent;
            return this;
        }

        public Builder opts(@Nullable String opts) {
            this.opts = opts;
            return this;
        }

        public Builder levelOffset(int levelOffset) {
            return levelOffset("+"  + levelOffset);
        }

        public Builder tag(String tag) {
            if (tags == null) {
                tags = new ArrayList<>();
            }
            tags.add(tag);
            return this;
        }

        public IncludeDirective build() {
            return new IncludeDirective(Objects.requireNonNull(target), levelOffset, lines, encoding, tags, indent, opts);
        }
    }
}
