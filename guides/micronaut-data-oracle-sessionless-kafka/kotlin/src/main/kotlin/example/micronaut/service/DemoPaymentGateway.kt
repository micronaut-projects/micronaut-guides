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
package example.micronaut.service

import example.micronaut.dto.PaymentRequested
import jakarta.inject.Singleton
import java.util.concurrent.ConcurrentHashMap

@Singleton // <1>
class DemoPaymentGateway {
    private val decisions = ConcurrentHashMap<Long, Decision>()

    data class Decision(val approved: Boolean, val reference: String)

    fun pay(request: PaymentRequested): Decision = decisions.computeIfAbsent(request.bookingId) { id ->
        val approved = request.paymentToken == "demo-approve"
        Decision(approved, if (approved) "demo-$id" else "DECLINED")
    }
}
