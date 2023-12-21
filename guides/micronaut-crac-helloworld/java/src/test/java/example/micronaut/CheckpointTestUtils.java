/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.context.ApplicationContext;
import io.micronaut.crac.test.CheckpointSimulator;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;

import java.util.function.Function;

public class CheckpointTestUtils {

    public static void test(Function<BlockingHttpClient, Object> testScenario) {
        try (EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class)) {
            CheckpointSimulator checkpointSimulator =
                    server.getApplicationContext().getBean(CheckpointSimulator.class);
            testApp(server, testScenario);

            checkpointSimulator.runBeforeCheckpoint();
            server.stop();
            checkpointSimulator.runAfterRestore();
            server.start();
            testApp(server, testScenario);
        }
    }

    public static Object testApp(EmbeddedServer embeddedServer, Function<BlockingHttpClient, Object> clientConsumer) {
        try (HttpClient httpClient = embeddedServer.getApplicationContext().createBean(HttpClient.class, embeddedServer.getURL())) {
            BlockingHttpClient client = httpClient.toBlocking();
            return clientConsumer.apply(client);
        }
    }
}
