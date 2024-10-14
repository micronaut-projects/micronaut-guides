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

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest // <1>
class SaasSubscriptionListJsonTest {

    @Autowired // <2>
    private JacksonTester<SaasSubscription> json; // <3>

    @Autowired  // <4>
    private JacksonTester<SaasSubscription[]> jsonList; // <5>
    private SaasSubscription[] saasSubscriptions;

    @BeforeEach
    void setUp() {
        saasSubscriptions = Arrays.array(
                new SaasSubscription(99L, "Advanced", 2900),
                new SaasSubscription(100L, "Essential", 1400),
                new SaasSubscription(101L, "Professional", 4900));
    }

    @Test
    void saasSubscriptionListSerializationTest() throws IOException {
        assertThat(jsonList.write(saasSubscriptions)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void saasSubscriptionListDeserializationTest() throws IOException {
        String expected = """
                            [
                            {"id": 99, "name": "Advanced", "cents": 2900},
                            {"id": 100, "name": "Essential", "cents": 1400},
                            {"id": 101, "name": "Professional", "cents": 4900}
                            ]""";
        assertThat(jsonList.parse(expected)).isEqualTo(saasSubscriptions);
    }
}