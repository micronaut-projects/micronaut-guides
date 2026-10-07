package example.micronaut

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

import static org.assertj.core.api.Assertions.assertThat

@JsonTest // <1>
class SaasSubscriptionJsonTest {

    @Autowired // <2>
    JacksonTester<SaasSubscription> json // <3>

    @Test
    void saasSubscriptionSerializationTest() {
        SaasSubscription subscription = new SaasSubscription(id: 99L, name: 'Professional', cents: 4900)
        assertThat(json.write(subscription)).isStrictlyEqualToJson('expected.json')
        assertThat(json.write(subscription)).hasJsonPathNumberValue('@.id')
        assertThat(json.write(subscription)).extractingJsonPathNumberValue('@.id')
                .isEqualTo(99)
        assertThat(json.write(subscription)).hasJsonPathStringValue('@.name')
        assertThat(json.write(subscription)).extractingJsonPathStringValue('@.name')
                .isEqualTo('Professional')
        assertThat(json.write(subscription)).hasJsonPathNumberValue('@.cents')
        assertThat(json.write(subscription)).extractingJsonPathNumberValue('@.cents')
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
