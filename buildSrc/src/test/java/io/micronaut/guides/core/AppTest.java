package io.micronaut.guides.core;

import io.micronaut.context.BeanContext;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.type.Argument;
import io.micronaut.guides.GuideMetadata;
import io.micronaut.json.JsonMapper;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import org.gradle.api.tasks.Input;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class AppTest {

    @Inject
    Validator validator;

    @Inject
    BeanContext beanContext;

    @Test
    void typeAppPackageNameCanBeNull() {
        String name = "name";
        String packageName = null;
        ApplicationType applicationType = ApplicationType.DEFAULT;
        String framework = "Micronaut";
        List<String> emptyList = new ArrayList<>();
        String testFramework = "testFramework";
        boolean validateLicense = true;

        Set<ConstraintViolation<App>> violations = validator.validate(
                new App(name,packageName,applicationType,framework,emptyList,emptyList,emptyList,emptyList,testFramework,emptyList, validateLicense));
        assertTrue(violations.isEmpty());
    }

    @Test
    void typeAppIsAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(App.class));
    }

    @Test
    void typeAppIsDeserializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(App.class)));
    }

    @Test
    void typeAppIsNotSerializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(App.class)));
    }

    @Test
    void buildGeneratesJsonSchema(ResourceLoader resourceLoader) {
        assertTrue(resourceLoader.getResource("META-INF/schemas/app.schema.json").isPresent());
    }

    @Test
    void buildGeneratesCorrectJsonSchema(ResourceLoader resourceLoader) throws IOException, JSONException {
        Optional<URL> res = resourceLoader.getResource("META-INF/schemas/app.schema.json");

        InputStream is = res.get().openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String schema = reader.lines().collect(Collectors.joining("\n"));
        String expected = """
            {
              "$schema": "https://json-schema.org/draft/2020-12/schema",
              "$id": "https://guides.micronaut.io/schemas/app.schema.json",
              "title": "App",
              "type": [
                "object"
              ],
              "properties": {
                "applicationType": {
                  "description": "The app type.  If you don't specify, default is used",
                  "type": [
                    "string"
                  ],
                  "enum": [
                    "DEFAULT",
                    "CLI",
                    "FUNCTION",
                    "GRPC",
                    "MESSAGING"
                  ]
                },
                "excludeTest": {
                  "description": "The tests that should not be run",
                  "type": [
                    "array"
                  ],
                  "items": {
                    "type": [
                      "string"
                    ]
                  }
                },
                "features": {
                  "description": "The Micronaut Starter features' name that the app requires",
                  "type": [
                    "array"
                  ],
                  "items": {
                    "type": [
                      "string"
                    ]
                  }
                },
                "framework": {
                  "description": "The app's framework. Default is Micronaut but Spring Boot is also supported",
                  "type": [
                    "string"
                  ]
                },
                "invisibleFeatures": {
                  "description": "The app's invisible features",
                  "type": [
                    "array"
                  ],
                  "items": {
                    "type": [
                      "string"
                    ]
                  }
                },
                "javaFeatures": {
                  "description": "The app's Java features",
                  "type": [
                    "array"
                  ],
                  "items": {
                    "type": [
                      "string"
                    ]
                  }
                },
                "kotlinFeatures": {
                  "description": "The app's Kotlin features",
                  "type": [
                    "array"
                  ],
                  "items": {
                    "type": [
                      "string"
                    ]
                  }
                },
                "name": {
                  "description": "The app's name. For single application guides, the application needs to be named default",
                  "type": [
                    "string"
                  ]
                },
                "packageName": {
                  "description": "The app's package name. If you don't specify, the package name example.micronaut is used",
                  "type": [
                    "string"
                  ]
                },
                "testFramework": {
                  "description": "The app's test framework",
                  "type": [
                    "string"
                  ]
                },
                "validateLicense": {
                  "description": "To enable Spotless code check",
                  "type": [
                    "boolean"
                  ]
                }
              },
              "required": [
                "name"
              ]
            }
        """;
        JSONAssert.assertEquals(expected, schema, JSONCompareMode.LENIENT);
    }

}
