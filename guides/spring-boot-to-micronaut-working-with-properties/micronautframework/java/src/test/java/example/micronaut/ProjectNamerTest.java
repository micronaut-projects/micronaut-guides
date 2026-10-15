package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
class ProjectNamerTest {
  @Inject
  ProjectNamer projectNamer;

  @Test
  void itIsPossibleToInjectWithAtValue() {
    assertEquals("PROPurple123", projectNamer.decorate("Purple"));
  }
}
