/*
 * Copyright 2017-2023 original authors original authors
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
package io.micronaut.guides.httpclient;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;

import java.util.List;
import java.util.Set;

@Introspected
public class Guide {

    @NonNull
    private final String title;

    @NonNull
    private final String intro;

    @NonNull
    private final Set<String> authors;

    @NonNull
    private final List<String> tags;

    @NonNull
    private final String category;

    @NonNull
    private final String publicationDate;

    @NonNull
    private final String slug;

    @NonNull
    private final String url;

    @NonNull
    private final List<Option> options;

    public Guide(@NonNull String title, @NonNull String intro, @NonNull Set<String> authors, @NonNull List<String> tags, @NonNull String category, @NonNull String publicationDate, @NonNull String slug, @NonNull String url, @NonNull List<Option> options) {
        this.title = title;
        this.intro = intro;
        this.authors = authors;
        this.tags = tags;
        this.category = category;
        this.publicationDate = publicationDate;
        this.slug = slug;
        this.url = url;
        this.options = options;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getIntro() {
        return intro;
    }

    @NonNull
    public Set<String> getAuthors() {
        return authors;
    }

    @NonNull
    public List<String> getTags() {
        return tags;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    @NonNull
    public String getPublicationDate() {
        return publicationDate;
    }

    @NonNull
    public String getSlug() {
        return slug;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @NonNull
    public List<Option> getOptions() {
        return options;
    }
}
