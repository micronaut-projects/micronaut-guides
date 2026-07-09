package example.micronaut

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest // <1>
class SaasSubscriptionJsonTest {

    @Autowired // <2>
    private lateinit var json: JacksonTester<SaasSubscription> // <3>

    @Test
    fun saasSubscriptionSerializationTest() {
        val subscription = SaasSubscription(99L, "Professional", 4900)
        assertThat(json.write(subscription)).isStrictlyEqualToJson("expected.json")
        assertThat(json.write(subscription)).hasJsonPathNumberValue("@.id")
        assertThat(json.write(subscription)).extractingJsonPathNumberValue("@.id")
            .isEqualTo(99)
        assertThat(json.write(subscription)).hasJsonPathStringValue("@.name")
        assertThat(json.write(subscription)).extractingJsonPathStringValue("@.name")
            .isEqualTo("Professional")
        assertThat(json.write(subscription)).hasJsonPathNumberValue("@.cents")
        assertThat(json.write(subscription)).extractingJsonPathNumberValue("@.cents")
            .isEqualTo(4900)
    }

    @Test
    fun saasSubscriptionDeserializationTest() {
        val expected = """
           {
               "id":100,
               "name": "Advanced",
               "cents":2900
           }
        """.trimIndent()
        assertThat(json.parse(expected))
            .isEqualTo(SaasSubscription(100L, "Advanced", 2900))
        assertThat(json.parseObject(expected).id).isEqualTo(100)
        assertThat(json.parseObject(expected).name).isEqualTo("Advanced")
        assertThat(json.parseObject(expected).cents).isEqualTo(2900)
    }
}
