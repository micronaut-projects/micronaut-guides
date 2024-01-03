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

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;

@Controller  // <1>
public class DemoController {

    private final DemoProducer demoProducer;

    public DemoController(DemoProducer demoProducer) {  // <2>
        this.demoProducer = demoProducer;
    }

    @Post("/demo") // <3>
    @Status(HttpStatus.NO_CONTENT)
    public void publishDemoMessages() {
        demoProducer.send("Demo message body");  // <4>
    }
}