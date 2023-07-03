package example.micronaut;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import io.micronaut.rabbitmq.connect.ChannelInitializer;
import jakarta.inject.Singleton;

import java.io.IOException;

@Singleton
public class ChannelPoolListener extends ChannelInitializer {

    @Override
    public void initialize(Channel channel, String name) throws IOException {
        channel.exchangeDeclare("micronaut", BuiltinExchangeType.DIRECT, true);

        channel.queueDeclare("inventory", true, false, false, null);
        channel.queueBind("inventory", "micronaut", "books.inventory");

        channel.queueDeclare("catalogue", true, false, false, null);
        channel.queueBind("catalogue", "micronaut", "books.catalogue");
    }
}
