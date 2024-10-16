package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import java.util.List;

@Controller("/api")
class AlbumController {
    private final PhotoServiceClient photoServiceClient;

    AlbumController(PhotoServiceClient photoServiceClient) {
        this.photoServiceClient = photoServiceClient;
    }

    @Get("/albums/{albumId}")
    public HttpResponse<Album> getAlbumById(@PathVariable Long albumId) {
        List<Photo> photos = photoServiceClient.getPhotos(albumId);
        return HttpResponse.ok(new Album(albumId, photos));
    }
}