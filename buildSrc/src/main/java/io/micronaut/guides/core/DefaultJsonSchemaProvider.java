package io.micronaut.guides.core;

import com.networknt.schema.*;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

@Singleton
public class DefaultJsonSchemaProvider implements JsonSchemaProvider {
    JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012, builder ->
            builder.schemaMappers(schemaMappers -> schemaMappers.mapPrefix("https://guides.micronaut.io/schemas", "classpath:META-INF/schemas"))
    );

    @Override
    @NonNull
    public JsonSchema getSchema() {
        SchemaValidatorsConfig.Builder builder = SchemaValidatorsConfig.builder();
        SchemaValidatorsConfig validatorsConfig = builder.build();
        return jsonSchemaFactory.getSchema(SchemaLocation.of("https://guides.micronaut.io/schemas/guide-metadata.schema.json"), validatorsConfig);
    }
}
