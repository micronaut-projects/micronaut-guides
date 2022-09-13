package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.objectstorage.response.UploadResponse;

/**
 * Upload API that all controllers must implement.
 */
public interface UploadApi {

    @Post(consumes = MediaType.MULTIPART_FORM_DATA, produces = MediaType.TEXT_PLAIN)
    HttpResponse<String> upload(CompletedFileUpload fileUpload);

    static HttpResponse<String> fromUploadResponse(UploadResponse<?> uploadResponse) {
        return HttpResponse
                .created(uploadResponse.getKey())
                .header("ETag", uploadResponse.getETag());
    }

}
