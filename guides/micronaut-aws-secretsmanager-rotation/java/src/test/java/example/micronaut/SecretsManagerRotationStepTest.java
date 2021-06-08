package example.micronaut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;

class SecretsManagerRotationStepSpec extends Specification {

    @ParameterizedTest(name = "for {0} string inferred SecretsManagerRotationStep should be {1}")
    @MethodSource("stepProvider")
    void shouldCalculateToPayValueForInvoice(String str, SecretsManagerRotationStep step) {
        assertTrue(SecretsManagerRotationStep.of(str).isPresent())
        assertEquals(SecretsManagerRotationStep.of(str).get(), step);
    }

    private static Stream<Arguments> stepProvider() {
        return Stream.of(
                Arguments.of("createSecret", SecretsManagerRotationStep.CREATE_SECRET),
                Arguments.of("setSecret", SecretsManagerRotationStep.SET_SECRET),
                Arguments.of("testSecret", SecretsManagerRotationStep.TEST_SECRET),
                Arguments.of("finishSecret", SecretsManagerRotationStep.FINISH_SECRET)
        );
    }
}
