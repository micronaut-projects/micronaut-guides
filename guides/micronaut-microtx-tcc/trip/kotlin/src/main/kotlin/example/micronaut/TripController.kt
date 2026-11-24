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
package example.micronaut

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.oracledb.microtx.tcc.MicroTxTccClient
import io.micronaut.oracledb.microtx.tcc.TCC
import io.micronaut.oracledb.microtx.tcc.TccResult
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import java.time.temporal.ChronoUnit

@ExecuteOn(TaskExecutors.BLOCKING) // <1>
@Controller("/trips")
open class TripController(
    private val hotel: HotelClient,
    private val tcc: MicroTxTccClient
) {

    @TCC(timeLimit = 2, timeUnit = ChronoUnit.MINUTES) // <2>
    @Post
    open fun book(@Body request: TripRequest): TccResult {
        hotel.reserve(BookingRequest(request.hotel)) // <3>
        return if (request.confirm) tcc.confirm() else tcc.cancel() // <4>
    }
}
