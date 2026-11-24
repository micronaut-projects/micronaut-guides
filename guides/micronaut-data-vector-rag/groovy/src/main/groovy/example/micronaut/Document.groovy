package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.VectorStorage
import io.micronaut.data.model.vector.FloatVector

@CompileStatic
@MappedEntity('documents')
class Document {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    Long id

    String content

    @VectorStorage(length = Embeddings.DIMENSIONS)
    FloatVector embedding
}
