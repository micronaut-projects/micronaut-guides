package example.micronaut;

import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.function.aws.MicronautRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import jakarta.inject.Inject;
import java.util.Locale;

@Serdeable
public class FunctionRequestHandler
        extends MicronautRequestHandler<S3EventNotification, Void> { // <1>
    private static final Logger LOG = LoggerFactory.getLogger(FunctionRequestHandler.class);
    private static final String SLASH = "/";
    private static final String THUMBNAILS = "thumbnails";
    public static final String OBJECT_CREATED = "ObjectCreated";
    private static final String JPG = "jpg";
    private static final String PNG = "png";
    private static final char DOT = '.';

    @Inject
    public S3Client s3Client; // <2>

    @Inject
    public ThumbnailGenerator thumbnailGenerator; // <3>

    @Override
    public Void execute(S3EventNotification input) {
        for (S3EventNotification.S3EventNotificationRecord record : input.getRecords()) {

            LOG.info("event name: {}" , record.getEventName());
            if (record.getEventName().contains(OBJECT_CREATED)) { // <4>
                S3EventNotification.S3Entity s3Entity = record.getS3();
                String bucket = s3Entity.getBucket().getName();
                String key = s3Entity.getObject().getKey();
                int index = key.lastIndexOf(DOT);
                if (index != -1) {
                    String format = key.substring(index + 1).toLowerCase(Locale.ENGLISH);
                    if (format.equals(PNG) || format.equals(JPG)) {
                        thumbnailGenerator.thumbnail(s3Client.getObject(GetObjectRequest.builder()
                                .bucket(bucket)
                                .key(key)
                                .build()), format)
                                .ifPresent(bytes -> s3Client.putObject(PutObjectRequest.builder()
                                        .key(thumbnailKey(key))
                                        .bucket(bucket)
                                        .build(), RequestBody.fromBytes(bytes)));
                    }
                }
            }
        }
        return null;
    }

    private static String thumbnailKey(String key) {
        int index = key.lastIndexOf(SLASH);
        String fileName = index != -1 ? key.substring(index + SLASH.length()) : key;
        return THUMBNAILS + SLASH + fileName;
    }
}
