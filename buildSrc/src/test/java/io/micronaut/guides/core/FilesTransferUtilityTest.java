package io.micronaut.guides.core;

import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class FilesTransferUtilityTest {

  @Inject
  FilesTransferUtility filesTransferUtility;

  @Inject
  JsonMapper jsonMapper;

  @Inject
  JsonSchemaProvider jsonSchemaProvider;


  @Inject
  GuideProjectGenerator guideProjectGenerator;

  @Test
  void testTransfer() throws Exception {
    String outputPath = "build/tmp/test";
    File outputDirectory = new File(outputPath);
    outputDirectory.mkdir();
    List<Guide> metadatas = new ArrayList<>();

    String pathBase = "src/test/resources/guides/hello-base";
    File fileBase = new File(pathBase);
    GuideUtils.parseGuideMetadata(fileBase,"metadata.json", jsonSchemaProvider.getSchema(), jsonMapper).ifPresent(metadatas::add);
    String path = "src/test/resources/guides/creating-your-first-micronaut-app";
    File file = new File(path);
    GuideUtils.parseGuideMetadata(file,"metadata.json", jsonSchemaProvider.getSchema(), jsonMapper).ifPresent(metadatas::add);

    guideProjectGenerator.generate(outputDirectory,metadatas.get(0));
    guideProjectGenerator.generate(outputDirectory,metadatas.get(1));

    filesTransferUtility.transferFiles(outputDirectory,fileBase,metadatas.get(0));
    filesTransferUtility.transferFiles(outputDirectory,file,metadatas.get(1));

    assertTrue(new File(outputPath+"/creating-your-first-micronaut-app-gradle-groovy").exists());
    assertTrue(new File(outputPath+"/creating-your-first-micronaut-app-gradle-java").exists());
    assertTrue(new File(outputPath+"/creating-your-first-micronaut-app-gradle-kotlin").exists());
    assertTrue(new File(outputPath+"/creating-your-first-micronaut-app-maven-groovy").exists());
    assertTrue(new File(outputPath+"/creating-your-first-micronaut-app-maven-java").exists());
    assertTrue(new File(outputPath+"/creating-your-first-micronaut-app-maven-kotlin").exists());

    assertFalse(new File(outputPath+"/hello-base-gradle-groovy").exists());
    assertFalse(new File(outputPath+"/hello-base-gradle-java").exists());
    assertFalse(new File(outputPath+"/hello-base-gradle-kotlin").exists());
    assertFalse(new File(outputPath+"/hello-base-maven-groovy").exists());
    assertFalse(new File(outputPath+"/hello-base-maven-java").exists());
    assertFalse(new File(outputPath+"/hello-base-maven-kotlin").exists());

    assertTrue(new File(outputPath+"/creating-your-first-micronaut-app-gradle-groovy/src/main/groovy/example/micronaut/HelloController.groovy").exists());
    assertTrue(new File(outputPath+"/creating-your-first-micronaut-app-gradle-java/src/main/java/example/micronaut/HelloController.java").exists());
    assertTrue(new File(outputPath+"/creating-your-first-micronaut-app-gradle-kotlin/src/main/kotlin/example/micronaut/HelloController.kt").exists());
    assertTrue(new File(outputPath+"/creating-your-first-micronaut-app-maven-groovy/src/main/groovy/example/micronaut/HelloController.groovy").exists());
    assertTrue(new File(outputPath+"/creating-your-first-micronaut-app-maven-java/src/main/java/example/micronaut/HelloController.java").exists());
    assertTrue(new File(outputPath+"/creating-your-first-micronaut-app-maven-kotlin/src/main/kotlin/example/micronaut/HelloController.kt").exists());

    assertFalse(new File(outputPath+"/creating-your-first-micronaut-app-gradle-groovy/src/main/groovy/example/micronaut/Application.groovy").exists());
    assertFalse(new File(outputPath+"/creating-your-first-micronaut-app-gradle-java/src/main/java/example/micronaut/Application.java").exists());
    assertFalse(new File(outputPath+"/creating-your-first-micronaut-app-gradle-kotlin/src/main/kotlin/example/micronaut/Application.kt").exists());
    assertFalse(new File(outputPath+"/creating-your-first-micronaut-app-maven-groovy/src/main/groovy/example/micronaut/Application.groovy").exists());
    assertFalse(new File(outputPath+"/creating-your-first-micronaut-app-maven-java/src/main/java/example/micronaut/Application.java").exists());
    assertFalse(new File(outputPath+"/creating-your-first-micronaut-app-maven-kotlin/src/main/kotlin/example/micronaut/Application.kt").exists());
  }

}
