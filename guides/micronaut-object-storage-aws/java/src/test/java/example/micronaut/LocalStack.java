/*
 * Copyright 2017-2024 original authors
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

import io.micronaut.core.util.CollectionUtils;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public class LocalStack {
    private static final String TAG = "1.1.0";
    private static final String IMAGE = "localstack/localstack";
    private static LocalStackContainer localstack;

    private static LocalStackContainer getLocalStack() {
        init();
        return localstack;
    }

    private static String getEndpoint() {
        return getLocalStack().getEndpointOverride(LocalStackContainer.Service.S3).toString();
    }

    private static String secretAccessKey() {
        return getLocalStack().getSecretKey();
    }

    private static String getRegion() {
        return getLocalStack().getRegion();
    }

    private static String accessKeyId() {
        return getLocalStack().getAccessKey();
    }

    public static Map<String, String> getProperties() {
        return CollectionUtils.mapOf(
                "aws.accessKeyId", accessKeyId(),
                "aws.secretKey", secretAccessKey(),
                "aws.region", getRegion(),
                "aws.services.s3.endpoint-override", getEndpoint()
        );
    }

    public static void init() {
        if (localstack == null) {
            localstack = new LocalStackContainer(DockerImageName.parse(IMAGE + ":" + TAG))
                    .withServices(LocalStackContainer.Service.S3);
            localstack.start();
        }
    }
    public static void close() {
        if (localstack != null) {
            localstack.close();
        }
    }
}
