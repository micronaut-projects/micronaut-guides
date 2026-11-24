package example.micronaut

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.VectorStorage
import io.micronaut.data.model.vector.FloatVector

@MappedEntity("documents")
data class Document(
    @field:Id @field:GeneratedValue(GeneratedValue.Type.IDENTITY) val id: Long?,
    val content: String,
    @field:VectorStorage(length = Embeddings.DIMENSIONS) val embedding: FloatVector
)
