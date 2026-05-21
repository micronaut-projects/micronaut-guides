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
import jakarta.inject.Singleton;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Singleton
public class DispatchService {

    private static final double MAX_DISTANCE_METERS = 5_000d; // <1>
    private static final double EARTH_RADIUS_METERS = 6_371_008.8d;

    private final DeliveryDriverRepository deliveryDriverRepository;

    public DispatchService(DeliveryDriverRepository deliveryDriverRepository) {
        this.deliveryDriverRepository = deliveryDriverRepository;
    }

    public Optional<DriverMatch> findClosestAvailableDriver(Point orderLocation) {
        List<DeliveryDriver> candidates = deliveryDriverRepository.findByStatusAndLocationNear(
            DeliveryDriver.AVAILABLE,
            orderLocation,
            MAX_DISTANCE_METERS
        ); // <2>

        return candidates.stream()
            .map(driver -> new DriverMatch(
                driver.getId(),
                driver.getName(),
                distanceMeters(orderLocation, driver.getLocation())
            ))
            .min(Comparator.comparingDouble(DriverMatch::distanceMeters)); // <3>
    }

    private double distanceMeters(Point left, Point right) { // <4>
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
