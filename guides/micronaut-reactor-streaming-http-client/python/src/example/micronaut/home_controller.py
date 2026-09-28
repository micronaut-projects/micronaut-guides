from java.net import URI, URL
from java.time import Duration
from jakarta.annotation import PreDestroy
from micronaut.core.io.buffer import ReferenceCounted
from micronaut.http import HttpRequest, MediaType
from micronaut.http.annotation import Controller, Get, Produces
from micronaut.http.client import DefaultHttpClientConfiguration
from micronaut.reactor.http.client import ReactorStreamingHttpClient
from org.reactivestreams import Publisher
from reactor.core.publisher import Flux

DEFAULT_URI = URI.create(
    "https://raw.githubusercontent.com/micronaut-projects/micronaut-guides/master/guides/micronaut-reactor-streaming-http-client/src/test/resources/micronaut5K.png"
)


@Controller  # <1>
class HomeController:
    def __init__(self):
        configuration = DefaultHttpClientConfiguration()
        configuration.setReadTimeout(Duration.ofSeconds(30))
        self.reactor_streaming_http_client = ReactorStreamingHttpClient.create(
            URL("https://guides.micronaut.io/"), configuration
        )  # <2>

    @Produces(MediaType.IMAGE_PNG)
    @Get  # <3>
    def download(self) -> Publisher:
        request = HttpRequest.GET(DEFAULT_URI)
        return Flux.from_(self.reactor_streaming_http_client.dataStream(request)).doOnNext(
            retain_reference_counted
        )  # <4>

    @PreDestroy  # <5>
    def close(self) -> None:
        if self.reactor_streaming_http_client is not None:
            self.reactor_streaming_http_client.close()


def retain_reference_counted(byte_buffer) -> None:
    if isinstance(byte_buffer, ReferenceCounted):
        byte_buffer.retain()
