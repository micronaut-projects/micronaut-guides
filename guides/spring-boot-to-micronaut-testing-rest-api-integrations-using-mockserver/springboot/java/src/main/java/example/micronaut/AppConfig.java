package example.micronaut;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AppConfig {

    @Bean
    public PhotoServiceClient photoServiceClient(
            @Value("${photos.api.base-url}") String photosApiBaseUrl
    ) {
        WebClient client = WebClient.builder().baseUrl(photosApiBaseUrl).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(client))
                .build();
        return factory.createClient(PhotoServiceClient.class);
    }
}