package example.micronaut

import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel
import io.micronaut.data.model.vector.FloatVector
import jakarta.inject.Singleton

@Singleton
class Embeddings {

    companion object {
        const val DIMENSIONS = 384
    }

    private val model = AllMiniLmL6V2EmbeddingModel()

    fun embed(text: String): FloatVector = FloatVector(model.embed(text).content().vector())
}
