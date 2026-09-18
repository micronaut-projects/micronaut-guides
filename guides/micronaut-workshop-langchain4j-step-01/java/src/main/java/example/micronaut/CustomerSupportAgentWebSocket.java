package example.micronaut;

import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;

@ServerWebSocket("/customer-support-agent")
public class CustomerSupportAgentWebSocket {

    private final CustomerSupportAgent customerSupportAgent;

    public CustomerSupportAgentWebSocket(CustomerSupportAgent customerSupportAgent) {
        this.customerSupportAgent = customerSupportAgent;
    }

    @OnOpen
    public void onOpen(WebSocketSession session) {
        session.sendSync("Welcome to Miles of Smiles! How can I help you today?");
    }

    @OnMessage
    public void onTextMessage(String message, WebSocketSession session) {
        session.sendSync(customerSupportAgent.chat(session.getId(), message));
    }
}
