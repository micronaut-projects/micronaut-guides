package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.objectstorage.aws.AwsS3Operations;
import io.micronaut.objectstorage.request.UploadRequest;
import io.micronaut.objectstorage.response.UploadResponse;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

/**
 * AWS-specific implementation of {@link UploadApi}.
 */
@Controller("/cloud")
public class AwsUploadController implements UploadApi {

    private final AwsS3Operations objectStorage;

    public AwsUploadController(AwsS3Operations objectStorage) {
        this.objectStorage = objectStorage;
    }

    @Override
    public HttpResponse<String> upload(CompletedFileUpload fileUpload) {
        UploadRequest objectStorageUpload = UploadRequest.fromCompletedFileUpload(fileUpload);
        UploadResponse<PutObjectResponse> response = objectStorage.upload(objectStorageUpload, builder -> {
            builder.acl(ObjectCannedACL.PUBLIC_READ);
        });

        return UploadApi.fromUploadResponse(response);
    }

}
