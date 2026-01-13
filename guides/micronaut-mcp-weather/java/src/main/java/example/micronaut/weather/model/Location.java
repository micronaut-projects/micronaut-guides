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
package example.micronaut.weather.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Location(double latitude, double longitude) {
    private static final Logger LOG = LoggerFactory.getLogger(Location.class);

    public static Optional<Location> of(Map<String, Object> arguments) {
        Object latitudeObj = arguments.get("latitude");
        Object longitudeObj = arguments.get("longitude");
        if (latitudeObj == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Missing 'latitude' in arguments");
            }
            return Optional.empty();
        }
        if (longitudeObj == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Missing 'longitude' in arguments");
            }
            return Optional.empty();
        }
        Double latitude = null;
        try {
            latitude = Double.valueOf(latitudeObj.toString());
        } catch(NumberFormatException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("latitude {} is not a number", latitudeObj);
            }
            return Optional.empty();
        }
        Double longitude = null;
        try {
            longitude = Double.valueOf(longitudeObj.toString());
        } catch(NumberFormatException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("longitude {} is not a number", longitudeObj);
            }
            return Optional.empty();
        }
        return Optional.of(new Location(latitude, longitude));
    }
}
