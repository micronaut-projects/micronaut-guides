/*
 * Copyright 2017-2026 original authors
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

import io.floci.testcontainers.FlociContainer;
import io.micronaut.core.util.CollectionUtils;

import java.util.Map;

public class Floci {
    private static FlociContainer floci;

    private static FlociContainer getFloci() {
        init();
        return floci;
    }

    private static String getEndpoint() {
        return getFloci().getEndpoint();
    }

    private static String secretAccessKey() {
        return getFloci().getSecretKey();
    }

    private static String getRegion() {
        return getFloci().getRegion();
    }

    private static String accessKeyId() {
        return getFloci().getAccessKey();
    }

    public static Map<String, String> getProperties() {
        return CollectionUtils.mapOf(
                "aws.accessKeyId", accessKeyId(),
                "aws.secretKey", secretAccessKey(),
                "aws.region", getRegion(),
                "aws.services.s3.endpoint-override", getEndpoint(),
                "aws.services.s3.path-style-access-enabled", "true"
        );
    }

    public static void init() {
        if (floci == null) {
            floci = new FlociContainer();
            floci.start();
        }
    }
    public static void close() {
        if (floci != null) {
            floci.close();
        }
    }
}
