from dev.langchain4j.model.embedding.onnx.allminilml6v2 import AllMiniLmL6V2EmbeddingModel
from jakarta.inject import Singleton

DIMENSIONS = 384


@Singleton
class Embeddings:
    def __init__(self):
        self._model = AllMiniLmL6V2EmbeddingModel()

    def embed(self, text: str) -> list[float]:
        return list(self._model.embed(text).content().vector())
