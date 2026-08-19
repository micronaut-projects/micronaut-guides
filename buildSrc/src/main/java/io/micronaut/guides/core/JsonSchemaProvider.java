package io.micronaut.guides.core;

import com.networknt.schema.Schema;
import io.micronaut.core.annotation.NonNull;

@FunctionalInterface
public interface JsonSchemaProvider {

    /**
     * @return Provides a JSON Schema used to validate guides.
     */
    @NonNull
    Schema getSchema();
}
