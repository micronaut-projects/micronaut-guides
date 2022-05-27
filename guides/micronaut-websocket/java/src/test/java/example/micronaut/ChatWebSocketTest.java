package example.micronaut;

import io.micronaut.core.beans.BeanMap;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.retry.annotation.Retryable;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.opentest4j.AssertionFailedError;

import java.util.Arrays;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD) // <1>
class ChatWebSocketTest {

    @Inject
    ChatClientConnect webSocketConnect; // <2>

    @Inject
    EmbeddedServer server; // <3>

    ChatClientEndpoint adam;
    ChatClientEndpoint anna;
    ChatClientEndpoint ben;
    ChatClientEndpoint cienna;
    ChatClientEndpoint zach;

    @BeforeEach
    public void setup() { // <4>
        UriBuilder uriBuilder = UriBuilder.of("ws://localhost").port(server.getPort()).path("/ws/chat/{topic}/{username}");
        adam = webSocketConnect.connect(uriBuilder.expand(BeanMap.of(new ChatRequest("adam", "Cats & Recreation"))));
        anna = webSocketConnect.connect(uriBuilder.expand(BeanMap.of(new ChatRequest("anna", "Cats & Recreation"))));
        ben = webSocketConnect.connect(uriBuilder.expand(BeanMap.of(new ChatRequest("ben", "Fortran Tips & Tricks"))));
        zach = webSocketConnect.connect(uriBuilder.expand(BeanMap.of(new ChatRequest("zach", "all"))));
        cienna = webSocketConnect.connect(uriBuilder.expand(BeanMap.of(new ChatRequest("cienna", "Fortran Tips & Tricks"))));
    }

    @Test
    @Retryable(AssertionFailedError.class)
    void should_broadcast_message_when_users_connect_to_specific_topic() throws InterruptedException { // <5>
        log("should_broadcast_message_when_users_connect_to_specific_topic");

        //TODO Problems getting @Retryable to work on tests
        Thread.sleep(1000);

        /*
         * Users will only see messages broadcast since they joined.
         * They can't see the chat history prior to joining a topic.
         * History could be implemented using some sort of persistence
         * or just holding the messages in memory on the server which is
         * fine in a contrived example but would complicate horizontal
         * scaling.
         */

        Assertions.assertEquals(
                Arrays.asList("[adam] Joined Cats & Recreation!", "[anna] Joined Cats & Recreation!", "[zach] Now making announcements!"),
                adam.getMessagesChronologically());

        Assertions.assertEquals(
                Arrays.asList("[anna] Joined Cats & Recreation!", "[zach] Now making announcements!"),
                anna.getMessagesChronologically());

        Assertions.assertEquals(
                Arrays.asList("[ben] Joined Fortran Tips & Tricks!", "[zach] Now making announcements!", "[cienna] Joined Fortran Tips & Tricks!"),
                ben.getMessagesChronologically());

        Assertions.assertEquals(
                Arrays.asList("[zach] Now making announcements!", "[cienna] Joined Fortran Tips & Tricks!"),
                zach.getMessagesChronologically());

        Assertions.assertEquals(
                Arrays.asList("[cienna] Joined Fortran Tips & Tricks!"),
                cienna.getMessagesChronologically());
    }

    @Test
    @Retryable(AssertionFailedError.class)
    void should_broadcast_message_to_all_users_inside_the_topic() throws InterruptedException { // <6>
        log("should_broadcast_message_to_all_users_inside_the_topic");

        final String adamsGreeting = "Hello, everyone. It's another purrrfect day :-)";
        final String expectedGreeting = "[adam] " + adamsGreeting;
        adam.broadcastChat(adamsGreeting);

        //TODO Problems getting @Retryable to work on tests
        Thread.sleep(1000);

        //subscribed to "Cats & Recreation"
        Assertions.assertEquals(expectedGreeting, adam.getLatestMessage());

        //subscribed to "Cats & Recreation"
        Assertions.assertEquals(expectedGreeting, anna.getLatestMessage());

        //NOT subscribed to "Cats & Recreation"
        Assertions.assertNotEquals(expectedGreeting, ben.getLatestMessage());

        //subscribed to the special "all" topic
        Assertions.assertEquals(expectedGreeting, zach.getLatestMessage());

        //NOT subscribed to "Cats & Recreation"
        Assertions.assertNotEquals(expectedGreeting, cienna.getLatestMessage());
    }

    @Test
    @Retryable(AssertionFailedError.class)
    void should_broadcast_message_when_user_disconnects_from_the_chat() throws Exception { // <7>
        log("should_broadcast_message_when_user_disconnects_from_the_chat");

        anna.close();

        //TODO Problems getting @Retryable to work on tests
        Thread.sleep(1000);

        String annaLeaving = "[anna] Leaving Cats & Recreation!";

        //subscribed to "Cats & Recreation"
        Assertions.assertEquals(annaLeaving, adam.getLatestMessage());

        //Anna already left and therefore won't see the message about her leaving
        Assertions.assertNotEquals(annaLeaving, anna.getLatestMessage());

        //NOT subscribed to "Cats & Recreation"
        Assertions.assertNotEquals(annaLeaving, ben.getLatestMessage());

        //subscribed to the special "all" topic
        Assertions.assertEquals(annaLeaving, zach.getLatestMessage());

        //NOT subscribed to "Cats & Recreation"
        Assertions.assertNotEquals(annaLeaving, cienna.getLatestMessage());
    }

    private void log(String test) {
        System.out.println(test + "...");
        System.out.println(adam.getMessagesChronologically());
        System.out.println(anna.getMessagesChronologically());
        System.out.println(ben.getMessagesChronologically());
        System.out.println(zach.getMessagesChronologically());
        System.out.println(cienna.getMessagesChronologically());
    }
}
