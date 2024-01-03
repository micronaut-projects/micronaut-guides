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
//tag::package[]
package example.micronaut;
//end::package[]

//tag::imports[]
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.views.View;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
//end::imports[]

//tag::clazz[]
@Controller("/books") // <1>
public class BookController {
//end::clazz[]

    //tag::di[]
    private final MessageSource messageSource;

    public BookController(MessageSource messageSource) { // <1>
        this.messageSource = messageSource;
    }
    //end::di[]

    //tag::create[]
    @View("bookscreate") // <2>
    @Get("/create") // <3>
    public Map<String, Object> create() {
        return createModelWithBlankValues();
    }
    //end::create[]

    //tag::stock[]
    @Produces(MediaType.TEXT_PLAIN)
    @Get("/stock/{isbn}")
    public Integer stock(String isbn) {
        throw new OutOfStockException();
    }
    //end::stock[]

    //tag::save[]
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <4>
    @Post("/save") // <5>
    public HttpResponse save(@Valid @Body CommandBookSave cmd) { // <6>
        return HttpResponse.ok();
    }
    //end::save[]

    //tag::onSavedFailed[]
    @View("bookscreate")
    @Error(exception = ConstraintViolationException.class) // <2>
    public Map<String, Object> onSavedFailed(HttpRequest request, ConstraintViolationException ex) { // <3>
        final Map<String, Object> model = createModelWithBlankValues();
        model.put("errors", messageSource.violationsMessages(ex.getConstraintViolations()));
        Optional<CommandBookSave> cmd = request.getBody(CommandBookSave.class);
        cmd.ifPresent(bookSave -> populateModel(model, bookSave));
        return model;
    }

    private void populateModel(Map<String, Object> model, CommandBookSave bookSave) {
        model.put("title", bookSave.getTitle());
        model.put("pages", bookSave.getPages());
    }

    //end::onSavedFailed[]

    //tag::createModelWithBlankValues[]
    private Map<String, Object> createModelWithBlankValues() {
        final Map<String, Object> model = new HashMap<>();
        model.put("title", "");
        model.put("pages", "");
        return model;
    }
    //end::createModelWithBlankValues[]
    //tag::endOfFile[]
}
//end::endOfFile[]