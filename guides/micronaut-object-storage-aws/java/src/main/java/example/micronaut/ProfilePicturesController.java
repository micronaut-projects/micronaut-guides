package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.objectstorage.aws.AwsS3ObjectStorageEntry;
import io.micronaut.objectstorage.aws.AwsS3Operations;
import io.micronaut.objectstorage.request.UploadRequest;
import io.micronaut.objectstorage.response.UploadResponse;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

@Controller(ProfilePicturesController.PREFIX)
@ExecuteOn(TaskExecutors.IO)
public class ProfilePicturesController implements ProfilePicturesApi {

    static final String PREFIX = "/pictures";
    private static final String ETAG_HEADER = "ETag";

    private final AwsS3Operations objectStorage;
    private final EmbeddedServer server;

    public ProfilePicturesController(AwsS3Operations objectStorage, EmbeddedServer server) {
        this.objectStorage = objectStorage;
        this.server = server;
    }

    @Override
    public HttpResponse upload(CompletedFileUpload fileUpload, String userId) {
        Optional<HttpResponse<String>> validationResult = validateFileUpload(fileUpload);
        if (validationResult.isPresent()) {
            return validationResult.get();
        }

        String key = buildKey(userId);
        UploadRequest objectStorageUpload = UploadRequest.fromCompletedFileUpload(fileUpload, key);
        UploadResponse<PutObjectResponse> response = objectStorage.upload(objectStorageUpload, builder -> {
            builder.acl(ObjectCannedACL.PUBLIC_READ);
        });

        return HttpResponse
                .created(location(userId))
                .header(ETAG_HEADER, response.getETag());
    }

    @Override
    public Optional<HttpResponse<StreamedFile>> download(String userId) {
        String key = buildKey(userId);
        return objectStorage.retrieve(key)
                .map(ProfilePicturesController::buildStreamedFile);
    }

    @Override
    public void delete(String userId) {
        String key = buildKey(userId);

        objectStorage.delete(key);
    }

    private static Optional<HttpResponse<String>> validateFileUpload(CompletedFileUpload fileUpload) {
        if (fileUpload.getContentType().isPresent()) {
            MediaType type = fileUpload.getContentType().get();
            if (!type.matches(MediaType.IMAGE_JPEG_TYPE)) {
                return Optional.of(HttpResponse.badRequest("Profile pictures must be in JPEG format"));
            }
        } else {
            return Optional.of(HttpResponse.badRequest("Content-Type header missing"));
        }
        return Optional.empty();
    }

    private static String buildKey(String userId) {
        return userId + ".jpg";
    }

    private static HttpResponse<StreamedFile> buildStreamedFile(AwsS3ObjectStorageEntry entry) {
        GetObjectResponse nativeEntry = entry.getNativeEntry();
        StreamedFile file = new StreamedFile(entry.getInputStream(), MediaType.of(nativeEntry.contentType())).attach(entry.getKey());
        MutableHttpResponse<Object> httpResponse = HttpResponse.ok().header(ETAG_HEADER, nativeEntry.eTag());
        file.process(httpResponse);
        return httpResponse.body(file);
    }

    private URI location(String userId) {
        String uriString = String.format("%s/%s", PREFIX, userId);
        try {
            return new URI(server.getScheme(), null, server.getHost(), server.getPort(), uriString, null, null);
        } catch (URISyntaxException e) {
            return URI.create(uriString);
        }
    }

}
