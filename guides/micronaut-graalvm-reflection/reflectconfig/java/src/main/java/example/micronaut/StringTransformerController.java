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
package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;

@Controller("/transformer") // <1>
public class StringTransformerController {

    private final StringTransformer transformer;

    public StringTransformerController(StringTransformer transformer) {  // <2>
        this.transformer = transformer;
    }

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get("/capitalize{?q}") // <4>
    String capitalize(@Nullable @QueryValue String q) { // <5>
        String className = "example.micronaut.StringCapitalizer";
        String methodName = "capitalize";
        return transformer.transform(q, className, methodName);
    }

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get("/reverse{?q}") // <4>
    String reverse(@Nullable @QueryValue String q) { // <5>
        String className = "example.micronaut.StringReverser";
        String methodName = "reverse";
        return transformer.transform(q, className, methodName);
    }

}
