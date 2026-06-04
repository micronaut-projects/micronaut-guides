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

@Singleton
class DispatchService(private val deliveryDriverRepository: DeliveryDriverRepository) {

    private companion object {
        private const val MAX_DISTANCE_METERS = 5_000.0 // <1>
        private const val WGS84_A = 6378137.0
        private const val WGS84_F = 1.0 / 298.257223563
        private const val EPS = 1e-15
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
        val lon1Deg = left.x()
        val lat1Deg = left.y()

        val lon2Deg = right.x()
        val lat2Deg = right.y()

        validate(lon1Deg, lat1Deg)
        validate(lon2Deg, lat2Deg)

        val lon1 = Math.toRadians(lon1Deg)
        val lat1 = Math.toRadians(lat1Deg)
        val lon2 = Math.toRadians(lon2Deg)
        val lat2 = Math.toRadians(lat2Deg)

        if (near(lon1, lon2) && near(lat1, lat2)) {
            return 0.0
        }

        val dLon = lon2 - lon1

        val sinLat1 = Math.sin(lat1)
        val cosLat1 = Math.cos(lat1)
        val sinLat2 = Math.sin(lat2)
        val cosLat2 = Math.cos(lat2)

        var cosD = sinLat1 * sinLat2 + cosLat1 * cosLat2 * Math.cos(dLon)
        cosD = cosD.coerceIn(-1.0, 1.0)

        val d = Math.acos(cosD)
        val sinD = Math.sin(d)

        val k = square(sinLat1 - sinLat2)
        val l = square(sinLat1 + sinLat2)

        val h = if (near(1.0 - cosD, 0.0)) 0.0 else (d + 3.0 * sinD) / (1.0 - cosD)
        val g = if (near(1.0 + cosD, 0.0)) 0.0 else (d - 3.0 * sinD) / (1.0 + cosD)

        val correction = -(WGS84_F / 4.0) * (h * k + g * l)

        return WGS84_A * (d + correction)
    }

    private fun validate(lon: Double, lat: Double) {
        require(java.lang.Double.isFinite(lon) && java.lang.Double.isFinite(lat)) {
            "Coordinates must be finite numbers"
        }
        require(lon > -180.0 && lon <= 180.0) {
            "Longitude must be in (-180, 180]"
        }
        require(lat >= -90.0 && lat <= 90.0) {
            "Latitude must be in [-90, 90]"
        }
    }

    private fun near(a: Double, b: Double): Boolean {
        return Math.abs(a - b) <= EPS
    }

    private fun square(x: Double): Double {
        return x * x
    }
}
