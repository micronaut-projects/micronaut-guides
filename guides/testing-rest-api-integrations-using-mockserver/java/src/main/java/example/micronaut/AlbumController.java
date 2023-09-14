package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.scheduling.annotation.ExecuteOn;

import static io.micronaut.scheduling.TaskExecutors.BLOCKING;
@Controller("/api") // <1>
class AlbumController {
    private final PhotoServiceClient photoServiceClient;

    AlbumController(PhotoServiceClient photoServiceClient) { // <2>
        this.photoServiceClient = photoServiceClient;
    }

    @ExecuteOn(BLOCKING) // <3>
    @Get("/albums/{albumId}") // <4>
    public Album getAlbumById(@PathVariable Long albumId) { // <5>
        return new Album(albumId, photoServiceClient.getPhotos(albumId));
    }
}