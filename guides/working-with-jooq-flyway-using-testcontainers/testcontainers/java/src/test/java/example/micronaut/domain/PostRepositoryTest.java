package example.micronaut.domain;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false) // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
@Testcontainers(disabledWithoutDocker = true) // <3>
class PostRepositoryTest extends AbstractTest {

    @Test
    void shouldGetPostById(PostRepository repository) {
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
