import java
from jakarta.inject import Singleton
from micronaut.data.model.vector import FloatVector

DIMENSIONS = 384
AllMiniLmL6V2EmbeddingModel = java.type(
    "dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel"
)


@Singleton
class Embeddings:
    def __init__(self):
        self.model = AllMiniLmL6V2EmbeddingModel()

    def embed(self, text: str) -> FloatVector:
        return FloatVector(self.model.embed(text).content().vector())
