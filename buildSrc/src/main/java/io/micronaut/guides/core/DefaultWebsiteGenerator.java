package io.micronaut.guides.core;

import com.networknt.schema.JsonSchema;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.json.JsonMapper;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Singleton
public class DefaultWebsiteGenerator implements WebsiteGenerator {
    private final JsonSchemaProvider jsonSchemaProvider;
    private final JsonMapper jsonMapper;
    private final List<MacroSubstitution> macroSubstitutions;

    public DefaultWebsiteGenerator(JsonSchemaProvider jsonSchemaProvider, JsonMapper jsonMapper, List<MacroSubstitution> macroSubstitutions) {
        this.jsonSchemaProvider = jsonSchemaProvider;
        this.jsonMapper = jsonMapper;
        this.macroSubstitutions = macroSubstitutions;
    }

    @Override
    public void generate(@NotNull @NonNull File guidesInputDirectory,
                  @NotNull @NonNull File outputDirectory,
                  @Nullable String slug) throws IOException {

        JsonSchema jsonSchema = jsonSchemaProvider.getSchema();
        List<Guide> guides = GuideUtils.parseGuidesMetadata(guidesInputDirectory, jsonSchema, jsonMapper);
        for (Guide guide : guides) {
            // Generate the project in a temporary directory
            // Copy the files within the guide directory in the directory where generate guide into.
            // ZIP that folder.
            // Generate the bash script to run the tests.
            String asciidoc = null; // the the asciidoc from the guide.
            //for (GuidesOption guidesOption : guide.options()) {
                for (MacroSubstitution macroSubstitution : macroSubstitutions) {
                    //asciidoc = macroSubstitution.substitute(asciidoc, guide);
                    // Evaluate if we can render the asciidoc programatically with an asciidoc library taking into account that it needs to be able reference correctly the source folder.
                }
            }

        }


    }
}
