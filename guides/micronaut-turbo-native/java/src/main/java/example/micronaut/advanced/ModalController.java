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
package example.micronaut.advanced;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import example.micronaut.model.ViewModel;
import io.micronaut.views.View;
import io.netty.handler.codec.http.HttpHeaderNames;

@Controller("/new")
class ModalController {

    @View("new")
    @Get(produces = {MediaType.TEXT_HTML}, consumes = {MediaType.TEXT_HTML})
    ViewModel index() {
        return new ViewModel("A Modal Webpage");
    }

    @Post(produces = {MediaType.TEXT_HTML}, consumes = {MediaType.APPLICATION_FORM_URLENCODED, MediaType.TEXT_HTML})
    HttpResponse<?> post() {
        return HttpResponse.status(HttpStatus.FOUND).header(HttpHeaderNames.LOCATION, "/success");
    }
}