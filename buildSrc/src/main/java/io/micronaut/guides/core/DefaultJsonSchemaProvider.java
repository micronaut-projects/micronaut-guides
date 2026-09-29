package io.micronaut.guides.core;

import com.networknt.schema.Schema;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SpecificationVersion;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

@Singleton
public class DefaultJsonSchemaProvider implements JsonSchemaProvider {
    SchemaRegistry schemaRegistry = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_2020_12, builder ->
            builder.schemaIdResolvers(schemaIdResolvers -> schemaIdResolvers.mapPrefix("https://micronaut-projects.github.io/micronaut-guides//schemas", "classpath:"))
    );

    @Override
    @NonNull
    public Schema getSchema() {
        return schemaRegistry.getSchema(SchemaLocation.of("https://micronaut-projects.github.io/micronaut-guides//schemas/guide-metadata.schema.json"));
    }
}
