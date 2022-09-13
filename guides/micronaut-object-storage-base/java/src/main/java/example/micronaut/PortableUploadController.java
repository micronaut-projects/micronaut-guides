package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.objectstorage.ObjectStorageOperations;
import io.micronaut.objectstorage.request.UploadRequest;
import io.micronaut.objectstorage.response.UploadResponse;

/**
 * Portable implementation of {@link UploadApi} that does not use any cloud-specific code.
 */
@SuppressWarnings("rawtypes")
@Controller("/portable")
public class PortableUploadController implements UploadApi {

    private final ObjectStorageOperations objectStorage;

    public PortableUploadController(ObjectStorageOperations objectStorage) {
        this.objectStorage = objectStorage;
    }

    @Override
    public HttpResponse<String> upload(CompletedFileUpload fileUpload) {
        UploadRequest request = UploadRequest.fromCompletedFileUpload(fileUpload);
        UploadResponse<?> response = objectStorage.upload(request);

        return UploadApi.fromUploadResponse(response);
    }
}
