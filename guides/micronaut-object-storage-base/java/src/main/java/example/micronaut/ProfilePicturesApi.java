package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;
import java.util.Optional;

//tag::class[]
public interface ProfilePicturesApi {

    HttpResponse<?> upload(CompletedFileUpload fileUpload, String userId, HttpRequest<?> request);

    Optional<HttpResponse<StreamedFile>> download(String userId);

    void delete(String userId);
}
//end::class[]

