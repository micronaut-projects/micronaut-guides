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

import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.OnClose
import io.micronaut.websocket.annotation.OnMessage
import io.micronaut.websocket.annotation.OnOpen
import io.micronaut.websocket.annotation.ServerWebSocket
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.function.Predicate

@ServerWebSocket('/ws/chat/{topic}/{username}') // <1>
class ChatServerWebSocket {

    private static final Logger LOG = LoggerFactory.getLogger(ChatServerWebSocket)

    private final WebSocketBroadcaster broadcaster

    ChatServerWebSocket(WebSocketBroadcaster broadcaster) { // <2>
        this.broadcaster = broadcaster
    }

    @OnOpen // <3>
    Publisher<String> onOpen(String topic, String username, WebSocketSession session) {
        log('onOpen', session, username, topic)
        if (topic == 'all') { // <4>
            return broadcaster.broadcast("[${username}] Now making announcements!", isValid(topic))
        }
        broadcaster.broadcast("[${username}] Joined ${topic}!", isValid(topic))
    }

    @OnMessage // <5>
    Publisher<String> onMessage(String topic, String username, String message, WebSocketSession session) {
        log('onMessage', session, username, topic)
        broadcaster.broadcast("[${username}] ${message}", isValid(topic))
    }

    @OnClose // <6>
    Publisher<String> onClose(String topic, String username, WebSocketSession session) {
        log('onClose', session, username, topic)
        broadcaster.broadcast("[${username}] Leaving ${topic}!", isValid(topic))
    }

    private void log(String event, WebSocketSession session, String username, String topic) {
        LOG.info('* WebSocket: {} received for session {} from \'{}\' regarding \'{}\'',
                event, session.id, username, topic)
    }

    private Predicate<WebSocketSession> isValid(String topic) { // <7>
        { s ->
            topic == 'all' //broadcast to all users
                    || 'all' == s.uriVariables.get('topic', String, null) //"all" subscribes to every topic
                    || topic.equalsIgnoreCase(s.uriVariables.get('topic', String, null)) //intra-topic chat
        } as Predicate<WebSocketSession>
    }
}
