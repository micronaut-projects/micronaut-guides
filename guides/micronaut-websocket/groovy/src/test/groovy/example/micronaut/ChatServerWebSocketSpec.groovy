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
package example.micronaut

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.util.CollectionUtils
import io.micronaut.http.uri.UriBuilder
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.websocket.WebSocketClient
import io.micronaut.websocket.annotation.ClientWebSocket
import io.micronaut.websocket.annotation.OnMessage
import jakarta.inject.Inject
import jakarta.validation.constraints.NotBlank
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.util.concurrent.ConcurrentLinkedDeque

@Property(name = 'spec.name', value = 'ChatWebSocketTest') // <1>
@MicronautTest // <2>
class ChatServerWebSocketSpec extends Specification {

    @Inject
    BeanContext beanContext

    @Inject
    EmbeddedServer embeddedServer

    @Requires(property = 'spec.name', value = 'ChatWebSocketTest') // <1>
    @ClientWebSocket // <3>
    static abstract class TestWebSocketClient implements AutoCloseable { // <4>

        private final Deque<String> messageHistory = new ConcurrentLinkedDeque<>()

        String getLatestMessage() {
            messageHistory.peekLast()
        }

        List<String> getMessagesChronologically() {
            new ArrayList<>(messageHistory)
        }

        @OnMessage // <5>
        void onMessage(String message) {
            messageHistory.add(message)
        }

        abstract void send(@NonNull @NotBlank String message) // <6>
    }

    private TestWebSocketClient createWebSocketClient(int port, String username, String topic) {
        WebSocketClient webSocketClient = beanContext.getBean(WebSocketClient)
        URI uri = UriBuilder.of('ws://localhost')
                .port(port)
                .path('ws')
                .path('chat')
                .path('{topic}')
                .path('{username}')
                .expand(CollectionUtils.mapOf('topic', topic, 'username', username))
        Publisher<TestWebSocketClient> client = webSocketClient.connect(TestWebSocketClient, uri) // <7>
        Flux.from(client).blockFirst()
    }

    void 'test websocket server'() {
        given:
        PollingConditions conditions = new PollingConditions(timeout: 10)

        when:
        TestWebSocketClient adam = createWebSocketClient(embeddedServer.port, 'adam', 'Cats & Recreation') // <8>

        then:
        conditions.eventually { // <9>
            adam.messagesChronologically == ['[adam] Joined Cats & Recreation!']
        }

        when:
        TestWebSocketClient anna = createWebSocketClient(embeddedServer.port, 'anna', 'Cats & Recreation')

        then:
        conditions.eventually { // <9>
            anna.messagesChronologically == ['[anna] Joined Cats & Recreation!']
        }
        conditions.eventually { // <9>
            adam.messagesChronologically == ['[adam] Joined Cats & Recreation!', '[anna] Joined Cats & Recreation!']
        }

        when:
        TestWebSocketClient ben = createWebSocketClient(embeddedServer.port, 'ben', 'Fortran Tips & Tricks')

        then:
        conditions.eventually { // <9>
            ben.messagesChronologically == ['[ben] Joined Fortran Tips & Tricks!']
        }

        when:
        TestWebSocketClient zach = createWebSocketClient(embeddedServer.port, 'zach', 'all')

        then:
        conditions.eventually { // <9>
            zach.messagesChronologically == ['[zach] Now making announcements!']
        }

        when:
        TestWebSocketClient cienna = createWebSocketClient(embeddedServer.port, 'cienna', 'Fortran Tips & Tricks')

        then:
        conditions.eventually { // <9>
            cienna.messagesChronologically == ['[cienna] Joined Fortran Tips & Tricks!']
        }
        conditions.eventually { // <9>
            ben.messagesChronologically == ['[ben] Joined Fortran Tips & Tricks!', '[zach] Now making announcements!', '[cienna] Joined Fortran Tips & Tricks!'] // <10>
        }

        when: // should broadcast message to all users inside the topic // <11>
        String adamsGreeting = "Hello, everyone. It's another purrrfect day :-)"
        String expectedGreeting = "[adam] $adamsGreeting"
        adam.send(adamsGreeting)

        then:
        //subscribed to "Cats & Recreation"
        conditions.eventually { adam.latestMessage == expectedGreeting } // <9>
        //subscribed to "Cats & Recreation"
        conditions.eventually { anna.latestMessage == expectedGreeting } // <9>
        //NOT subscribed to "Cats & Recreation"
        ben.latestMessage != expectedGreeting
        //subscribed to the special "all" topic
        conditions.eventually { zach.latestMessage == expectedGreeting } // <9>
        //NOT subscribed to "Cats & Recreation"
        cienna.latestMessage != expectedGreeting

        when: // should broadcast message when user disconnects from the chat // <12>
        anna.close()
        String annaLeaving = '[anna] Leaving Cats & Recreation!'

        then:
        conditions.eventually { adam.latestMessage == annaLeaving } // <9>
        //subscribed to "Cats & Recreation"
        adam.latestMessage == annaLeaving
        //Anna already left and therefore won't see the message about her leaving
        anna.latestMessage != annaLeaving
        //NOT subscribed to "Cats & Recreation"
        ben.latestMessage != annaLeaving
        //subscribed to the special "all" topic
        zach.latestMessage == annaLeaving
        //NOT subscribed to "Cats & Recreation"
        cienna.latestMessage != annaLeaving

        cleanup:
        adam.close()
        ben.close()
        zach.close()
        cienna.close()
    }
}
