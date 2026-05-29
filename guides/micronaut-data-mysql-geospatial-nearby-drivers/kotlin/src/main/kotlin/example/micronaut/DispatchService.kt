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
import jakarta.inject.Singleton
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Singleton
class DispatchService(private val deliveryDriverRepository: DeliveryDriverRepository) {

    private companion object {
        private const val MAX_DISTANCE_METERS = 5_000.0 // <1>
        private const val EARTH_RADIUS_METERS = 6_371_008.8
    }

    fun findClosestAvailableDriver(orderLocation: Point): DriverMatch? {
        val candidates = deliveryDriverRepository.findByStatusAndLocationNear(
            DeliveryDriver.Status.AVAILABLE,
            orderLocation,
            MAX_DISTANCE_METERS
        ) // <2>

        return candidates
            .map { driver ->
                DriverMatch(
                    driver.id,
                    driver.name,
                    distanceMeters(orderLocation, driver.location)
                )
            }
            .minByOrNull { it.distanceMeters } // <3>
    }

    private fun distanceMeters(left: Point, right: Point): Double { // <4>
        val leftLatitude = Math.toRadians(left.y())
        val rightLatitude = Math.toRadians(right.y())
        val deltaLatitude = Math.toRadians(right.y() - left.y())
        val deltaLongitude = Math.toRadians(right.x() - left.x())

        val a = sin(deltaLatitude / 2) * sin(deltaLatitude / 2) +
            cos(leftLatitude) * cos(rightLatitude) *
            sin(deltaLongitude / 2) * sin(deltaLongitude / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS_METERS * c
    }
}
