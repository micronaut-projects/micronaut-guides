package example.micronaut;

import io.micronaut.context.annotation.ConfigurationBuilder;
import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("aws")
public class SqsConfig {

    private String accessKeyId;
    private String secretKey;
    private String region;

    @ConfigurationBuilder(configurationPrefix = "services.sqs")
    final Sqs sqs = new Sqs();

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Sqs getSqs() {
        return sqs;
    }

    public static class Sqs {

        private String endpointOverride;

        public String getEndpointOverride() {
            return endpointOverride;
        }

        public void setEndpointOverride(String endpointOverride) {
            this.endpointOverride = endpointOverride;
        }
    }
}
