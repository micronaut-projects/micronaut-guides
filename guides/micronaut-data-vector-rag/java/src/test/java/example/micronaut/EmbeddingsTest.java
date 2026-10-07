package example.micronaut;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmbeddingsTest {

    @Test
    void createsMiniLmEmbedding() {
        skipNativeImage();
        assertEquals(Embeddings.DIMENSIONS, new Embeddings().embed("Micronaut Data").toFloatArray().length);
    }

    @Test
    void recognizesSemanticSimilarity() {
        skipNativeImage();
        var embeddings = new Embeddings();
        var query = embeddings.embed("lightweight Java toolkit for backend services").toFloatArray();
        var framework = embeddings.embed("Micronaut is a JVM framework for fast microservices").toFloatArray();
        var gardening = embeddings.embed("Tomatoes grow well in sunny gardens").toFloatArray();

        assertTrue(dot(query, framework) > dot(query, gardening));
    }

    private static double dot(float[] left, float[] right) {
        double result = 0;
        for (int i = 0; i < left.length; i++) {
            result += left[i] * right[i];
        }
        return result;
    }

    private static void skipNativeImage() {
        Assumptions.assumeFalse(System.getProperty("org.graalvm.nativeimage.imagecode") != null);
    }
}
