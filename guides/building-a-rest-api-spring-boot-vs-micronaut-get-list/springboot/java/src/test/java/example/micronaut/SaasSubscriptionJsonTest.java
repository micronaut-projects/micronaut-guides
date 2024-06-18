/*
 * Copyright 2017-2024 original authors
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
package example.micronaut;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest // <1>
class SaasSubscriptionJsonTest {

    @Autowired // <2>
    private JacksonTester<SaasSubscription> json; // <3>

    @Test
    void saasSubscriptionSerializationTest() throws IOException {
        SaasSubscription subscription = new SaasSubscription(99L, "Professional", 4900);
        assertThat(json.write(subscription)).isStrictlyEqualToJson("expected.json");
        assertThat(json.write(subscription)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(subscription)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);
        assertThat(json.write(subscription)).hasJsonPathStringValue("@.name");
        assertThat(json.write(subscription)).extractingJsonPathStringValue("@.name")
                .isEqualTo("Professional");
        assertThat(json.write(subscription)).hasJsonPathNumberValue("@.cents");
        assertThat(json.write(subscription)).extractingJsonPathNumberValue("@.cents")
                .isEqualTo(4900);
    }

    @Test
    void saasSubscriptionDeserializationTest() throws IOException {
        String expected = """
           {
               "id":100,
               "name": "Advanced",
               "cents":2900
           }
           """;
        assertThat(json.parse(expected))
                .isEqualTo(new SaasSubscription(100L, "Advanced", 2900));
        assertThat(json.parseObject(expected).id()).isEqualTo(100);
        assertThat(json.parseObject(expected).name()).isEqualTo("Advanced");
        assertThat(json.parseObject(expected).cents()).isEqualTo(2900);
    }
}