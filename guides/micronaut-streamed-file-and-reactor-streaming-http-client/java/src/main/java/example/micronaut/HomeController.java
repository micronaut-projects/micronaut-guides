package example.micronaut;

import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.reactor.http.client.ReactorStreamingHttpClient;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
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

    @Get("{?q}") // <3>
    StreamedFile download(@Nullable @QueryValue String q) throws IOException { // <4>
        URI uri = q == null ? DEFAULT_URI : URI.create(q);
        HttpRequest<?> request = HttpRequest.GET(uri);

        LOG.trace("downloading: {}", uri);
        PipedOutputStream outputStream = new PipedOutputStream();
        dataStreamToOutputStream(request, outputStream, () -> LOG.trace("finished download"));

        PipedInputStream inputStream = new PipedInputStream(1024*10);
        inputStream.connect(outputStream);

        return new StreamedFile(inputStream, MediaType.IMAGE_PNG_TYPE);
    }

    private void dataStreamToOutputStream(HttpRequest<?> request,
                                          PipedOutputStream outputStream,
                                          Runnable finallyRunnable) {
        reactorStreamingHttpClient.dataStream(request) // <5>
                .doOnNext(byteBuffer -> {
                    LOG.trace("Saving byte array");
                    try {
                        outputStream.write(byteBuffer.toByteArray());
                    } catch (IOException e) {
                        LOG.error("IO Exception closing the stream", e);
                    }
                })
                .doFinally(signalType -> {
                    try {
                        LOG.trace("Closing OutputStream");
                        outputStream.close();
                    } catch (IOException e) {
                        LOG.error("IO Exception closing the stream", e);
                        System.out.println();
                    }
                    finallyRunnable.run();
                })
                .subscribe();
    }

    @PreDestroy // <6>
    @Override
    public void close() {
        if (reactorStreamingHttpClient != null) {
            reactorStreamingHttpClient.close();
        }
    }
}
