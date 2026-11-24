package example.micronaut

import dev.langchain4j.model.embedding.EmbeddingModel
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel
import groovy.transform.CompileStatic
import io.micronaut.data.model.vector.FloatVector
import jakarta.inject.Singleton

@CompileStatic
@Singleton
class Embeddings {

    static final int DIMENSIONS = 384
    private final EmbeddingModel model = new AllMiniLmL6V2EmbeddingModel()

    FloatVector embed(String text) {
        new FloatVector(model.embed(text).content().vector())
    }
}
