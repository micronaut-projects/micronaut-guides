package example.micronaut;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.VectorStorage;
import io.micronaut.data.model.vector.FloatVector;

@MappedEntity("documents")
public record Document(
        @Id @GeneratedValue(GeneratedValue.Type.IDENTITY) Long id,
        String content,
        @VectorStorage(length = Embeddings.DIMENSIONS) FloatVector embedding
) {
}
