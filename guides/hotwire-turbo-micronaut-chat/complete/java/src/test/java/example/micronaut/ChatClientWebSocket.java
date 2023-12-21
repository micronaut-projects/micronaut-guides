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

import io.micronaut.http.HttpRequest;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

@ClientWebSocket("/chat/{room}")
public abstract class ChatClientWebSocket implements AutoCloseable {

    private WebSocketSession session;
    private HttpRequest<?> request;
    private String room;
    private Collection<String> replies = new ConcurrentLinkedQueue<>();

    @OnOpen
    public void onOpen(String room,
                       WebSocketSession session,
                       HttpRequest<?> request) {
        this.room = room;
        this.session = session;
        this.request = request;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public HttpRequest<?> getRequest() {
        return request;
    }

    public String getRoom() {
        return room;
    }

    @OnMessage
    public void onMessage(String message) {
        replies.add(message);
    }

    public Collection<String> getReplies() {
        return replies;
    }
}
