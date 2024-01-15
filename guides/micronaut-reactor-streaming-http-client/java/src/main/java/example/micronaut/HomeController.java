package example.micronaut;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.core.io.buffer.ReferenceCounted;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.reactor.http.client.ReactorStreamingHttpClient;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

@Controller // <1>
class HomeController implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
    private static final URI DEFAULT_URI = URI.create("https://guides.micronaut.io/micronaut5K.png");

    private final ReactorStreamingHttpClient reactorStreamingHttpClient;

    HomeController() {
        String urlStr = "https://guides.micronaut.io/";
        URL url;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            throw new ConfigurationException("malformed URL" + urlStr);
        }
        this.reactorStreamingHttpClient = ReactorStreamingHttpClient.create(url); // <2>
    }

    @Get // <3>
    Flux<ByteBuffer<?>> download() {
        HttpRequest<?> request = HttpRequest.GET(DEFAULT_URI);
        return reactorStreamingHttpClient.dataStream(request).doOnNext(bb -> {
            if (bb instanceof ReferenceCounted rc) {
                rc.retain();
            }
        }); // <4>
    }

    @PreDestroy // <5>
    @Override
    public void close() {
        if (reactorStreamingHttpClient != null) {
            reactorStreamingHttpClient.close();
        }
    }
}
