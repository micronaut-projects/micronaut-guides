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
package example.micronaut.service;

import example.micronaut.dto.PaymentRequested;

import jakarta.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton // <1>
public class DemoPaymentGateway {
    private final Map<Long, Decision> decisions = new ConcurrentHashMap<>();

    public record Decision(boolean approved, String reference) {
    }

    public Decision pay(PaymentRequested request) {
        return decisions.computeIfAbsent(request.bookingId(), id -> {
            boolean approved = "demo-approve".equals(request.paymentToken());
            return new Decision(approved, approved ? "demo-" + id : "DECLINED");
        });
    }
}
