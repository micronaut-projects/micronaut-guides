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

import groovy.transform.CompileStatic
import io.micronaut.data.model.geo.Point
import jakarta.inject.Singleton

@CompileStatic
@Singleton
class DispatchService {

    private static final double MAX_DISTANCE_METERS = 5_000d // <1>
    private static final double WGS84_A = 6378137.0d
    private static final double WGS84_F = 1.0d / 298.257223563d
    private static final double EPS = 1e-15d

    private final DeliveryDriverRepository deliveryDriverRepository

    DispatchService(DeliveryDriverRepository deliveryDriverRepository) {
        this.deliveryDriverRepository = deliveryDriverRepository
    }

    DriverMatch findClosestAvailableDriver(Point orderLocation) {
        List<DeliveryDriver> candidates = deliveryDriverRepository.findByStatusAndLocationNear(
            DeliveryDriver.Status.AVAILABLE,
            orderLocation,
            MAX_DISTANCE_METERS
        ) // <2>

        DriverMatch closest = null
        for (DeliveryDriver driver : candidates) {
            DriverMatch match = new DriverMatch(
                driver.id,
                driver.name,
                distanceMeters(orderLocation, driver.location)
            )
            if (closest == null || match.distanceMeters < closest.distanceMeters) {
                closest = match
            }
        }
        return closest // <3>
    }

    private static double distanceMeters(Point left, Point right) { // <4>
        double lon1Deg = left.x()
        double lat1Deg = left.y()

        double lon2Deg = right.x()
        double lat2Deg = right.y()

        validate(lon1Deg, lat1Deg)
        validate(lon2Deg, lat2Deg)

        double lon1 = Math.toRadians(lon1Deg)
        double lat1 = Math.toRadians(lat1Deg)
        double lon2 = Math.toRadians(lon2Deg)
        double lat2 = Math.toRadians(lat2Deg)

        if (near(lon1, lon2) && near(lat1, lat2)) {
            return 0.0d
        }

        double dLon = lon2 - lon1

        double sinLat1 = Math.sin(lat1)
        double cosLat1 = Math.cos(lat1)
        double sinLat2 = Math.sin(lat2)
        double cosLat2 = Math.cos(lat2)

        double cosD = sinLat1 * sinLat2 + cosLat1 * cosLat2 * Math.cos(dLon)
        cosD = Math.max(-1.0d, Math.min(1.0d, cosD))

        double d = Math.acos(cosD)
        double sinD = Math.sin(d)

        double k = square(sinLat1 - sinLat2)
        double l = square(sinLat1 + sinLat2)

        double h = near(1.0d - cosD, 0.0d) ? 0.0d : (d + 3.0d * sinD) / (1.0d - cosD)
        double g = near(1.0d + cosD, 0.0d) ? 0.0d : (d - 3.0d * sinD) / (1.0d + cosD)

        double correction = -(WGS84_F / 4.0d) * (h * k + g * l)

        WGS84_A * (d + correction)
    }

    private static void validate(double lon, double lat) {
        if (!Double.isFinite(lon) || !Double.isFinite(lat)) {
            throw new IllegalArgumentException('Coordinates must be finite numbers')
        }
        if (lon <= -180.0d || lon > 180.0d) {
            throw new IllegalArgumentException('Longitude must be in (-180, 180]')
        }
        if (lat < -90.0d || lat > 90.0d) {
            throw new IllegalArgumentException('Latitude must be in [-90, 90]')
        }
    }

    private static boolean near(double a, double b) {
        Math.abs(a - b) <= EPS
    }

    private static double square(double x) {
        x * x
    }
}
