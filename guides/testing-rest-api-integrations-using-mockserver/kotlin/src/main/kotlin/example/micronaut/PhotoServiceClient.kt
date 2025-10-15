package example.micronaut

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client(id = "photosapi")
interface PhotoServiceClient {

    @Get("/albums/{albumId}/photos")
    fun getPhotos(@PathVariable albumId: Long): List<Photo>
}