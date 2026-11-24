import math

from example.micronaut.embeddings import DIMENSIONS, Embeddings


def test_creates_minilm_embedding():
    assert len(Embeddings().embed("Micronaut Data")) == DIMENSIONS


def test_recognizes_semantic_similarity():
    embeddings = Embeddings()
    query = embeddings.embed("lightweight Java toolkit for backend services")
    framework = embeddings.embed("Micronaut is a JVM framework for fast microservices")
    gardening = embeddings.embed("Tomatoes grow well in sunny gardens")

    assert math.sumprod(query, framework) > math.sumprod(query, gardening)
