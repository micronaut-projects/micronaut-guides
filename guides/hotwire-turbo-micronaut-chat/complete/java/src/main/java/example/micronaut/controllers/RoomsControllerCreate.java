/*
 * Copyright 2017-2023 original authors
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
package example.micronaut.controllers;

import example.micronaut.repositories.RoomRepository;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.views.View;

import java.util.Collections;
import java.util.Map;

@Controller("/rooms") // <1>
public class RoomsControllerCreate extends RoomsController {

    public RoomsControllerCreate(RoomRepository roomRepository) { // <2>
        super(roomRepository);
    }

    @View("/rooms/create") // <3>
    @Get("/create") // <4>
    @Produces(MediaType.TEXT_HTML) // <5>
    Map<String, Object> create() {
        return Collections.emptyMap();
    }
}
