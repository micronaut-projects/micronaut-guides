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

import io.micronaut.data.model.geo.Point
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue

@Controller("/orders") // <1>
class DeliveryDispatchController(private val dispatchService: DispatchService) {

    @Get("/nearest-driver") // <2>
    fun nearestDriver(@QueryValue longitude: Double, @QueryValue latitude: Double): DriverMatch? { // <3>
        return dispatchService.findClosestAvailableDriver(Point(longitude, latitude))
    }
}
