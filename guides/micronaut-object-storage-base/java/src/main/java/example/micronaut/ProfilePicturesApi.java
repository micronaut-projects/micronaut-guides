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

import java.net.URI;
import java.util.Optional;

public interface ProfilePicturesApi {

    @Post(uri = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA)
    HttpResponse upload(CompletedFileUpload fileUpload, String userId);

    @Get("/{userId}")
    Optional<HttpResponse<StreamedFile>> download(String userId);

    @Delete("/{userId}")
    void delete(String userId);

}
