package example.micronaut.domain;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(startApplication = false) // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
@Testcontainers(disabledWithoutDocker = true) // <3>
class UserRepositoryTest extends AbstractTest {

    @Inject
    UserRepository repository;

    @Test
    void shouldCreateUserSuccessfully() {
        User user = new User(null, "John", "john@gmail.com");

        User savedUser = repository.createUser(user);

        assertThat(savedUser.id()).isNotNull();
        assertThat(savedUser.name()).isEqualTo("John");
        assertThat(savedUser.email()).isEqualTo("john@gmail.com");

        repository.deleteUser(savedUser.id());
    }

    @Test
    void shouldGetUserByEmail() {
        User user = repository.getUserByEmail("siva@gmail.com").orElseThrow();

        assertThat(user.id()).isEqualTo(1L);
        assertThat(user.name()).isEqualTo("Siva");
        assertThat(user.email()).isEqualTo("siva@gmail.com");
    }


}
