package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.objectstorage.ObjectStorageEntry;
import io.micronaut.objectstorage.response.UploadResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Status;
import java.net.URI;
import java.util.Optional;

//tag::class[]
public interface ProfilePicturesApi {

    @Post(uri = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA) // <1>
    HttpResponse upload(CompletedFileUpload fileUpload, String userId, HttpRequest<?> request);

    @Get("/{userId}") // <2>
    Optional<HttpResponse<StreamedFile>> download(String userId);

    @Status(HttpStatus.NO_CONTENT) // <3>
    @Delete("/{userId}") // <4>
    void delete(String userId);
}
//end::class[]

