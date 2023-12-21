/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;

@Requires(notEnv = Environment.TEST) // <1>
@RabbitListener // <2>
public class AnalyticsListener {

    private final AnalyticsService analyticsService; // <3>

    public AnalyticsListener(AnalyticsService analyticsService) { // <3>
        this.analyticsService = analyticsService;
    }

    @Queue("analytics") // <4>
    public void updateAnalytics(Book book) {
        analyticsService.updateBookAnalytics(book); // <5>
    }
}
