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
package example.micronaut;

import io.micronaut.context.BeanContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.JsonMapper;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.websocket.WebSocketClient;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnMessage;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
@SuppressWarnings("unchecked")
class GraphQLSubscriptionTest {

    @Inject
    BeanContext beanContext;

    @Inject
    EmbeddedServer embeddedServer;

    @Inject
    JsonMapper jsonMapper;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void subscriptionReceivesNewMessages() throws Exception {
        try (GraphQLWsClient webSocketClient = createWebSocketClient()) {
            webSocketClient.send("{\"type\":\"connection_init\"}");

            Map<String, Object> ack = nextResponse(webSocketClient);
            assertEquals("connection_ack", ack.get("type"));

            webSocketClient.send("""
                    {"id":"chat-stream","type":"subscribe","payload":{"query":"subscription { stream { from text } }"}}
                    """.trim());

            client.toBlocking().exchange(
                    HttpRequest.POST("/graphql", "{\"query\":\"mutation { chat(from:\\\"Ada\\\", text:\\\"Hello GraphQL\\\") { from text } }\"}"),
                    Argument.STRING
            );

            Map<String, Object> next = nextResponse(webSocketClient);
            assertEquals("next", next.get("type"));
            Map<String, Object> payload = (Map<String, Object>) next.get("payload");
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            Map<String, Object> stream = (Map<String, Object>) data.get("stream");
            assertEquals("Ada", stream.get("from"));
            assertEquals("Hello GraphQL", stream.get("text"));

            webSocketClient.send("{\"id\":\"chat-stream\",\"type\":\"complete\"}");
        }
    }

    private GraphQLWsClient createWebSocketClient() {
        WebSocketClient webSocketClient = beanContext.getBean(WebSocketClient.class);
        URI uri = URI.create("ws://localhost:" + embeddedServer.getPort() + "/graphql-ws");
        return Flux.from(webSocketClient.connect(GraphQLWsClient.class, uri)).blockFirst();
    }

    private Map<String, Object> nextResponse(GraphQLWsClient client) throws InterruptedException, IOException {
        String message = client.nextResponse();
        assertNotNull(message);
        return jsonMapper.readValue(message, Argument.mapOf(String.class, Object.class));
    }

    @ClientWebSocket(uri = "${graphql.graphql-ws.path:/graphql-ws}", subprotocol = "graphql-transport-ws")
    abstract static class GraphQLWsClient implements AutoCloseable {

        private final BlockingQueue<String> responses = new ArrayBlockingQueue<>(10);

        @OnMessage
        void onMessage(String message) {
            responses.add(message);
        }

        abstract void send(String message);

        String nextResponse() throws InterruptedException {
            return responses.poll(5, TimeUnit.SECONDS);
        }
    }
}
