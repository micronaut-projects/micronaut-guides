package example.micronaut

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

import static org.assertj.core.api.Assertions.assertThat

@JsonTest
class SaasSubscriptionJsonTest {

    @Autowired
    JacksonTester<SaasSubscription> json

    @Test
    void saasSubscriptionSerializationTest() {
        SaasSubscription subscription = new SaasSubscription(id: 99L, name: 'Professional', cents: 4900)
        def result = json.write(subscription)
        assertThat(result).isStrictlyEqualToJson('expected.json')
        assertThat(result).hasJsonPathNumberValue('@.id')
        assertThat(result).extractingJsonPathNumberValue('@.id')
                .isEqualTo(99)
        assertThat(result).hasJsonPathStringValue('@.name')
        assertThat(result).extractingJsonPathStringValue('@.name')
                .isEqualTo('Professional')
        assertThat(result).hasJsonPathNumberValue('@.cents')
        assertThat(result).extractingJsonPathNumberValue('@.cents')
                .isEqualTo(4900)
    }

    @Test
    void saasSubscriptionDeserializationTest() {
        String expected = '''
           {
               "id":100,
               "name": "Advanced",
               "cents":2900
           }
           '''
        assertThat(json.parse(expected))
                .isEqualTo(new SaasSubscription(id: 100L, name: 'Advanced', cents: 2900))
        assertThat(json.parseObject(expected).id).isEqualTo(100)
        assertThat(json.parseObject(expected).name).isEqualTo('Advanced')
        assertThat(json.parseObject(expected).cents).isEqualTo(2900)
    }
}
