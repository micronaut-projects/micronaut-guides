package io.micronaut.guides.core;

import io.micronaut.context.BeanContext;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class GuideTest {

    @Inject
    Validator validator;

    @Inject
    BeanContext beanContext;

    @Test
    void typeCloudCanBeNull() {
        Set<ConstraintViolation<Guide>> violations = validator.validate(
                new Guide(null,null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null,null,null,null));
        assertTrue(violations.isEmpty());
    }

    @Test
    void typeAppIsAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(Guide.class));
    }

    @Test
    void typeGuideIsDeserializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(Guide.class)));
    }

    @Test
    void typeGuideIsNotSerializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(Guide.class)));
    }

    @Test
    void buildGeneratesJsonSchema(ResourceLoader resourceLoader) {
        assertTrue(resourceLoader.getResource("META-INF/schemas/guide.schema.json").isPresent());
    }


    @Test
    void buildGeneratesCorrectJsonSchema(ResourceLoader resourceLoader) throws IOException, JSONException {
        Optional<URL> res = resourceLoader.getResource("META-INF/schemas/guide.schema.json");

        InputStream is = res.get().openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String schema = reader.lines().collect(Collectors.joining("\n"));
        String expected = """
                    {
                      "$schema": "https://json-schema.org/draft/2020-12/schema",
                      "$id": "https://guides.micronaut.io/schemas/guide.schema.json",
                      "title": "Guide",
                      "type": [
                        "object"
                      ],
                      "properties": {
                        "apps": {
                          "$ref": "https://guides.micronaut.io/schemas/app.schema.json",
                          "description": "Applications created for the guide."
                        },
                        "asciidoctor": {
                          "description": "The guide asciidoc file. If not specified, the guide slug followed by the .adoc suffix is used",
                          "type": [
                            "string"
                          ]
                        },
                        "authors": {
                          "description": "The guide's authors",
                          "type": [
                            "array"
                          ],
                          "items": {
                            "type": [
                              "string"
                            ]
                          }
                        },
                        "base": {
                          "description": "Defaults to null; if set, indicates directory name of the base guide to copy before copying the current one",
                          "type": [
                            "string"
                          ]
                        },
                        "buildTools": {
                          "description": "By default the code in the guide is generated for Gradle and Maven. If a guide is specific only for a build tool, define it here",
                          "type": [
                            "array"
                          ],
                          "items": {
                            "type": [
                              "string"
                            ],
                            "enum": [
                              "MAVEN",
                              "GRADLE",
                              "GRADLE_KOTLIN"
                            ]
                          }
                        },
                        "categories": {
                          "description": "The guide's categories",
                          "type": [
                            "array"
                          ],
                          "items": {
                            "type": [
                              "string"
                            ]
                          }
                        },
                        "cloud": {
                          "description": "The acronym for the cloud service provider of the guide. For example, OCI for Oracle Cloud Infrastructure",
                          "type": [
                            "string"
                          ],
                          "enum": [
                            "OCI",
                            "AWS",
                            "AZURE",
                            "GCP"
                          ]
                        },
                        "env": {
                          "description": "The guide's environment variables",
                          "type": [
                            "object"
                          ],
                          "additionalProperties": {
                            "type": [
                              "string"
                            ]
                          }
                        },
                        "intro": {
                          "description": "The guide introduction",
                          "type": [
                            "string"
                          ]
                        },
                        "languages": {
                          "description": "The guide supported languages",
                          "type": [
                            "array"
                          ],
                          "items": {
                            "type": [
                              "string"
                            ],
                            "enum": [
                              "JAVA",
                              "GROOVY",
                              "KOTLIN"
                            ]
                          }
                        },
                        "maximumJavaVersion": {
                          "description": "If the guide needs a maximum Java version, define it here",
                          "type": [
                            "integer"
                          ]
                        },
                        "minimumJavaVersion": {
                          "description": "If the guide needs a minimum Java version, define it here",
                          "type": [
                            "integer"
                          ]
                        },
                        "publicationDate": {
                          "description": "The guide publication date. It should follow the format YYYY-MM-DD",
                          "type": [
                            "string"
                          ],
                          "format": "date"
                        },
                        "publish": {
                          "description": "Whether the guide should be published, it defaults to true. You can set it to false for draft or base guides.",
                          "type": [
                            "boolean"
                          ]
                        },
                        "skipGradleTests": {
                          "description": "Set it to true to skip running the tests for the Gradle applications for the guide",
                          "type": [
                            "boolean"
                          ]
                        },
                        "skipMavenTests": {
                          "description": "Set it to true to skip running the tests for the Maven applications for the guide",
                          "type": [
                            "boolean"
                          ]
                        },
                        "slug": {
                          "description": "The guide's slug. If not specified, the guides folder is used.",
                          "type": [
                            "string"
                          ]
                        },
                        "tags": {
                          "description": "List of tags added to the guide. features are added automatically as tags. No need to repeat them here",
                          "type": [
                            "array"
                          ],
                          "items": {
                            "type": [
                              "string"
                            ]
                          }
                        },
                        "testFramework": {
                          "description": "The guide's test framework. By default Java and Kotlin applications are tested with JUnit5 and Groovy applications with Spock",
                          "type": [
                            "string"
                          ],
                          "enum": [
                            "SPOCK",
                            "JUNIT"
                          ]
                        },
                        "title": {
                          "description": "The guide's title",
                          "type": [
                            "string"
                          ]
                        },
                        "zipIncludes": {
                          "description": "List of additional files with a relative path to include in the generated zip file for the guide",
                          "type": [
                            "array"
                          ],
                          "items": {
                            "type": [
                              "string"
                            ]
                          }
                        }
                      },
                      "required": [
                        "title",
                        "intro",
                        "authors",
                        "categories",
                        "publicationDate"
                      ]
                    }
                """;
        JSONAssert.assertEquals(expected, schema, JSONCompareMode.LENIENT);
    }


}
