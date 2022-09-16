package example.micronaut;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;
import io.micronaut.http.server.util.HttpHostResolver;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.objectstorage.aws.AwsS3ObjectStorageEntry;
import io.micronaut.objectstorage.aws.AwsS3Operations;
import io.micronaut.objectstorage.request.UploadRequest;
import io.micronaut.objectstorage.response.UploadResponse;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.net.URI;
import java.util.Optional;

//tag::begin-class[]
@Controller(ProfilePicturesController.PREFIX) // <1>
@ExecuteOn(TaskExecutors.IO) // <2>
public class ProfilePicturesController implements ProfilePicturesApi {

    static final String PREFIX = "/pictures";

    private final AwsS3Operations objectStorage; // <3>
    private final HttpHostResolver httpHostResolver; // <4>

    public ProfilePicturesController(AwsS3Operations objectStorage, HttpHostResolver httpHostResolver) {
        this.objectStorage = objectStorage;
        this.httpHostResolver = httpHostResolver;
    }
//end::begin-class[]

    //tag::upload[]
    @Override
    @Post(uri = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA) // <1>
    public HttpResponse<?> upload(CompletedFileUpload fileUpload, String userId, HttpRequest<?> request) {
        String key = buildKey(userId); // <2>
        UploadRequest objectStorageUpload = UploadRequest.fromCompletedFileUpload(fileUpload, key); // <3>
        UploadResponse<PutObjectResponse> response = objectStorage.upload(objectStorageUpload, builder -> {  // <4>
            builder.acl(ObjectCannedACL.PUBLIC_READ); // <5>
        });

        return HttpResponse
                .created(location(request, userId)) // <6>
                .header(HttpHeaders.ETAG, response.getETag()); // <7>
    }

    private static String buildKey(String userId) {
        return userId + ".jpg";
    }

    private URI location(HttpRequest<?> request, String userId) {
        return UriBuilder.of(PREFIX)
                .path(userId)
                .host(httpHostResolver.resolve(request))
                .build();
    }
    //end::upload[]

    //tag::download[]
    @Override
    @Get("/{userId}") // <1>
    public Optional<HttpResponse<StreamedFile>> download(String userId) {
        String key = buildKey(userId);
        return objectStorage.retrieve(key) // <2>
                .map(ProfilePicturesController::buildStreamedFile); // <3>
    }

    private static HttpResponse<StreamedFile> buildStreamedFile(AwsS3ObjectStorageEntry entry) {
        GetObjectResponse nativeEntry = entry.getNativeEntry();
        MediaType mediaType = MediaType.of(nativeEntry.contentType());
        StreamedFile file = new StreamedFile(entry.getInputStream(), mediaType).attach(entry.getKey());
        MutableHttpResponse<Object> httpResponse = HttpResponse.ok()
                .header(HttpHeaders.ETAG, nativeEntry.eTag()); // <4>
        file.process(httpResponse);
        return httpResponse.body(file);
    }
    //end::download[]

    //tag::delete[]
    @Override
    @Delete("/{userId}") // <1>
    public void delete(String userId) {
        String key = buildKey(userId);
        objectStorage.delete(key);
    }
    //end::delete[]

//tag::end-class[]
}
//end::end-class[]
