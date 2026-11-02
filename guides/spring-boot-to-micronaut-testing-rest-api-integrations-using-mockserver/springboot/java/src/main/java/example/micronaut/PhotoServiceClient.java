package example.micronaut;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

interface PhotoServiceClient {
    @GetExchange("/albums/{albumId}/photos")
    List<Photo> getPhotos(@PathVariable Long albumId);
}