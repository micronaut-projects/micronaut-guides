/*
 * Copyright 2017-2024 original authors
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

import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.convert.format.MapFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EachProperty("micronaut.gateway.routes")
public class RouteConfiguration implements Route {

    private final String name;
    private String uri;

    private int order = 0;

    private Map<String, Object> predicates;

    private List<String> rolesAllowed = new ArrayList<>();

    public RouteConfiguration(@Parameter String name) {
        this.name = name;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    @NonNull
    public List<String> getRolesAllowed() {
        return rolesAllowed;
    }

    public void setRolesAllowed(@NonNull List<String> rolesAllowed) {
        this.rolesAllowed = rolesAllowed;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public List<? extends RoutePredicate> getPredicates() {
        Object path = predicates.get("path");
        return List.of((ConfigurationRoutePredicate) () -> path != null ? path.toString() : null);
    }

    public void setPredicates(@MapFormat(transformation = MapFormat.MapTransformation.FLAT) Map<String, Object> predicates) {
        this.predicates = predicates;
    }



}
