package io.micronaut.guides.core;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
public class FilesCopyUtilityTest {

  @Inject
  FilesCopyUtility filesCopyUtility;

  @Test
  void testCopy(){

  }

}
