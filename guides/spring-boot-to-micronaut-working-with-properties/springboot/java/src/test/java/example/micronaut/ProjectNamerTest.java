package example.micronaut;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ProjectNamerTest {
  @Autowired
  ProjectNamer projectNamer;

  @Test
  void itIsPossibleToInjectWithAtValue() {
    assertEquals("PROPurple123", projectNamer.decorate("Purple"));
  }
}
