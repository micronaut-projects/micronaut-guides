package example.micronaut

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

import static org.assertj.core.api.Assertions.assertThat

@JsonTest // <1>
class SaasSubscriptionListJsonTest {

    @Autowired // <2>
    JacksonTester<SaasSubscription> json // <3>

    @Autowired // <4>
    JacksonTester<SaasSubscription[]> jsonList // <5>
    private SaasSubscription[] saasSubscriptions

    @BeforeEach
    void setUp() {
        saasSubscriptions = [
                new SaasSubscription(id: 99L, name: 'Advanced', cents: 2900),
                new SaasSubscription(id: 100L, name: 'Essential', cents: 1400),
                new SaasSubscription(id: 101L, name: 'Professional', cents: 4900)
        ] as SaasSubscription[]
    }

    @Test
    void saasSubscriptionListSerializationTest() {
        assertThat(jsonList.write(saasSubscriptions)).isStrictlyEqualToJson('list.json')
    }

    @Test
    void saasSubscriptionListDeserializationTest() {
        String expected = '''
                            [
                            {"id": 99, "name": "Advanced", "cents": 2900},
                            {"id": 100, "name": "Essential", "cents": 1400},
                            {"id": 101, "name": "Professional", "cents": 4900}
                            ]'''
        assertThat(jsonList.parse(expected)).isEqualTo(saasSubscriptions)
    }
}
