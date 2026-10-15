package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;

import java.util.List;

public interface RssFeedGenerator {
    @NonNull
    String rssFeed(@NonNull List<Guide> metadatas);
}
