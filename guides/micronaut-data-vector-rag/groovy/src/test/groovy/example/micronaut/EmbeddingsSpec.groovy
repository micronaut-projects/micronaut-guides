package example.micronaut

import spock.lang.Specification

class EmbeddingsSpec extends Specification {

    void 'creates MiniLM embedding'() {
        expect:
        new Embeddings().embed('Micronaut Data').toFloatArray().length == Embeddings.DIMENSIONS
    }

    void 'recognizes semantic similarity'() {
        given:
        def embeddings = new Embeddings()
        def query = embeddings.embed('lightweight Java toolkit for backend services').toFloatArray()
        def framework = embeddings.embed('Micronaut is a JVM framework for fast microservices').toFloatArray()
        def gardening = embeddings.embed('Tomatoes grow well in sunny gardens').toFloatArray()

        expect:
        dot(query, framework) > dot(query, gardening)
    }

    private static double dot(float[] left, float[] right) {
        double result = 0
        for (int i = 0; i < left.length; i++) {
            result += left[i] * right[i]
        }
        result
    }
}
