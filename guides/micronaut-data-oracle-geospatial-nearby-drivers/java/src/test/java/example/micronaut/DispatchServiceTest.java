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

import io.micronaut.data.model.geo.Point;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false) // <1>
class DispatchServiceTest {

    private static final double EARTH_RADIUS_METERS = 6_371_008.8d;

    @Inject
    DeliveryDriverRepository deliveryDriverRepository;

    @Inject
    DispatchService dispatchService;

    @BeforeEach
    void clean() {
        deliveryDriverRepository.deleteAll();
    }

    @Test
    void findsClosestAvailableDriverWithinFiveKilometers() {
        Point orderLocation = new Point(-73.9857d, 40.7484d);
        DeliveryDriver nearby = deliveryDriverRepository.save(new DeliveryDriver(
            "Nearby Driver",
            DeliveryDriver.AVAILABLE,
            new Point(-73.9757d, 40.7554d)
        ));
        DeliveryDriver closest = deliveryDriverRepository.save(new DeliveryDriver(
            "Closest Driver",
            DeliveryDriver.AVAILABLE,
            new Point(-73.9827d, 40.7504d)
        ));
        deliveryDriverRepository.save(new DeliveryDriver(
            "Busy Driver",
            DeliveryDriver.BUSY,
            new Point(-73.9850d, 40.7488d)
        )); // <2>
        deliveryDriverRepository.save(new DeliveryDriver(
            "Far Driver",
            DeliveryDriver.AVAILABLE,
            new Point(-73.9000d, 40.8000d)
        )); // <3>

        Optional<DriverMatch> match = dispatchService.findClosestAvailableDriver(orderLocation);

        assertTrue(match.isPresent());
        assertEquals(closest.getId(), match.get().driverId()); // <4>
        assertTrue(match.get().distanceMeters() < distanceMeters(orderLocation, nearby.getLocation()));
    }

    @Test
    void returnsEmptyWhenNoAvailableDriverIsCloseEnough() {
        Point orderLocation = new Point(-73.9857d, 40.7484d);
        deliveryDriverRepository.save(new DeliveryDriver(
            "Busy Driver",
            DeliveryDriver.BUSY,
            new Point(-73.9850d, 40.7488d)
        ));
        deliveryDriverRepository.save(new DeliveryDriver(
            "Far Driver",
            DeliveryDriver.AVAILABLE,
            new Point(-73.9000d, 40.8000d)
        ));

        assertTrue(dispatchService.findClosestAvailableDriver(orderLocation).isEmpty());
    }

    private static double distanceMeters(Point left, Point right) {
        double leftLatitude = Math.toRadians(left.y());
        double rightLatitude = Math.toRadians(right.y());
        double deltaLatitude = Math.toRadians(right.y() - left.y());
        double deltaLongitude = Math.toRadians(right.x() - left.x());

        double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2)
            + Math.cos(leftLatitude) * Math.cos(rightLatitude)
            * Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_METERS * c;
    }
}
