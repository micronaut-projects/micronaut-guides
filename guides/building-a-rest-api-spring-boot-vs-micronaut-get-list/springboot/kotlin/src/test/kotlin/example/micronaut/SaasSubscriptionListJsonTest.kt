/*
 * Copyright 2017-2026 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest // <1>
class SaasSubscriptionListJsonTest {

    @Autowired // <2>
    lateinit var json: JacksonTester<SaasSubscription> // <3>

    @Autowired // <4>
    lateinit var jsonList: JacksonTester<Array<SaasSubscription>> // <5>

    private lateinit var saasSubscriptions: Array<SaasSubscription>

    @BeforeEach
    fun setUp() {
        saasSubscriptions = arrayOf(
            SaasSubscription(99, "Advanced", 2900),
            SaasSubscription(100, "Essential", 1400),
            SaasSubscription(101, "Professional", 4900)
        )
    }

    @Test
    fun saasSubscriptionListSerializationTest() {
        assertThat(jsonList.write(saasSubscriptions)).isStrictlyEqualToJson("list.json")
    }

    @Test
    fun saasSubscriptionListDeserializationTest() {
        val expected = """
            [
            {"id": 99, "name": "Advanced", "cents": 2900},
            {"id": 100, "name": "Essential", "cents": 1400},
            {"id": 101, "name": "Professional", "cents": 4900}
            ]
        """.trimIndent()
        assertThat(jsonList.parseObject(expected)).containsExactly(*saasSubscriptions)
    }
}
