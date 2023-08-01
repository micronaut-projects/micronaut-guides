package example.micronaut.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.jdbc.Sql;

@JooqTest(
        properties = {"spring.test.database.replace=none", "spring.datasource.url=jdbc:tc:postgresql:15.3-alpine:///db"
        })
@Sql("/test-data.sql")
class UserRepositoryJooqTest {

    @Autowired
    DSLContext dsl;

    UserRepository repository;

    @BeforeEach
    void setUp() {
        this.repository = new UserRepository(dsl);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        User user = new User(null, "John", "john@gmail.com");

        User savedUser = repository.createUser(user);

        assertThat(savedUser.id()).isNotNull();
        assertThat(savedUser.name()).isEqualTo("John");
        assertThat(savedUser.email()).isEqualTo("john@gmail.com");
    }

    @Test
    void shouldGetUserByEmail() {
        User user = repository.getUserByEmail("siva@gmail.com").orElseThrow();

        assertThat(user.id()).isEqualTo(1L);
        assertThat(user.name()).isEqualTo("Siva");
        assertThat(user.email()).isEqualTo("siva@gmail.com");
    }
}
