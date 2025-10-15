package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

@Controller("/api") // <1>
class AlbumController(private val photoServiceClient: PhotoServiceClient) { // <2>

    @ExecuteOn(TaskExecutors.BLOCKING) // <3>
    @Get("/albums/{albumId}") // <4>
    fun getAlbumById(@PathVariable albumId: Long): Album { // <5>
        return Album(albumId, photoServiceClient.getPhotos(albumId))
    }
}