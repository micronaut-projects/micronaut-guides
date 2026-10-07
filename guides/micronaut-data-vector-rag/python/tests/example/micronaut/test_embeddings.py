from example.micronaut.embeddings import DIMENSIONS, Embeddings


def test_creates_minilm_embedding():
    assert len(Embeddings().embed("Micronaut Data").toFloatArray()) == DIMENSIONS


def test_recognizes_semantic_similarity():
    embeddings = Embeddings()
    query = embeddings.embed("lightweight Java toolkit for backend services").toFloatArray()
    framework = embeddings.embed("Micronaut is a JVM framework for fast microservices").toFloatArray()
    gardening = embeddings.embed("Tomatoes grow well in sunny gardens").toFloatArray()

    assert sum(a * b for a, b in zip(query, framework)) > sum(
        a * b for a, b in zip(query, gardening)
    )
