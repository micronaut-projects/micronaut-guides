/*
 * Copyright 2017-2025 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Validated
@ConfigurationProperties("greeting")
public class GreetingConfiguration {

    @NotNull
    private String message;

    @NotNull
    private String suffix = "!";

    private String name;

    private final ContentConfig content = new ContentConfig();

    public String getMessage() { return this.message; }
    public void setMessage(String message) { this.message = message; }

    public String getSuffix() { return this.suffix; }
    public void setSuffix(String suffix) { this.suffix = suffix; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public ContentConfig getContent() { return this.content; }

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