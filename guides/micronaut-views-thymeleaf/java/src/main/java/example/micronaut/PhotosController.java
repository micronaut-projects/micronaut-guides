package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.View;

import java.util.Collections;
import java.util.Map;

@Controller("/photos") // <1>
public class PhotosController {

    private final PhotosClient photosClient;

    public PhotosController(PhotosClient photosClient) { // <2>
        this.photosClient = photosClient;
    }

    @Produces(MediaType.TEXT_HTML) // <3>
    @ExecuteOn(TaskExecutors.BLOCKING) // <4>
    @View("photos/show.html") // <5>
    @Get("/{id}") // <6>
    Map<String, Photo> findById(@PathVariable Long id) { // <7>
        return Collections.singletonMap("photo", photosClient.findById(id));
    }
}
