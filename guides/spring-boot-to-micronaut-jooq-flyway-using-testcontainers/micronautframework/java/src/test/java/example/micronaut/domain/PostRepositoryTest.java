package example.micronaut.domain;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers(disabledWithoutDocker = true)
class PostRepositoryTest extends AbstractTest {
    @Inject
    PostRepository repository;

    @Test
    void shouldGetPostById() {
        Optional<Post> postOptional = repository.getPostById(1L);
        assertTrue(postOptional.isPresent());
        Post post = postOptional.get();

        assertThat(post.id()).isEqualTo(1L);
        assertThat(post.title()).isEqualTo("Post 1 Title");
        assertThat(post.content()).isEqualTo("Post 1 content");
        assertThat(post.createdBy().id()).isEqualTo(1L);
        assertThat(post.createdBy().name()).isEqualTo("Siva");
        assertThat(post.createdBy().email()).isEqualTo("siva@gmail.com");
        assertThat(post.comments()).hasSize(2);
    }
}
