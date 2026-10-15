package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;

import java.io.IOException;
import java.util.List;

public interface JsonFeedGenerator {
    @NonNull
    String jsonFeedString(@NonNull List<Guide> metadatas) throws IOException;
}
