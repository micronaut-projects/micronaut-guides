package example.micronaut;

import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@ConfigurationProperties("greeting")
public class GreetingConfiguration {

    @NotNull
    private String message;

    @NotNull
    private String suffix = "!";

    private String name;

    private ContentConfig content = new ContentConfig();

    public String getMessage() { return this.message; }
    public void setMessage(String message) { this.message = message; }

    public String getSuffix() { return this.suffix; }
    public void setSuffix(String suffix) { this.suffix = suffix; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public void setContent(ContentConfig content) {
        this.content = content;
    }

    public ContentConfig getContent() { return this.content; }

    @ConfigurationProperties("content")
    public static class ContentConfig {
        @NotNull
        @Positive
        private Integer prizeAmount;

        @NotEmpty
        private List<String> recipients;

        public Integer getPrizeAmount() {
            return this.prizeAmount;
        }

        public void setPrizeAmount(Integer prizeAmount) {
            this.prizeAmount = prizeAmount;
        }

        public List<String> getRecipients() {
            return this.recipients;
        }

        public void setRecipients(List<String> recipients) {
            this.recipients = recipients;
        }
    }
}