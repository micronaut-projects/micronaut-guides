package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;
import java.util.Optional;

//tag::class[]
public interface ProfilePicturesApi {

    @Post(uri = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA) // <1>
    HttpResponse upload(CompletedFileUpload fileUpload, String userId, HttpRequest<?> request);

    @Get("/{userId}") // <2>
    Optional<HttpResponse<StreamedFile>> download(String userId);

    @Delete("/{userId}") // <3>
    void delete(String userId);
}
//end::class[]

