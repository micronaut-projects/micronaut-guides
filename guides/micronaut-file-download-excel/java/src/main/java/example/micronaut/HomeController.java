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

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.types.files.SystemFile;
import io.micronaut.views.View;

import java.util.HashMap;
import java.util.Map;

@Controller // <1>
public class HomeController {

    private final BookRepository bookRepository;
    private final BookExcelService bookExcelService;

    public HomeController(BookRepository bookRepository,  // <2>
                          BookExcelService bookExcelService) {
        this.bookRepository = bookRepository;
        this.bookExcelService = bookExcelService;
    }

    @View("index") // <3>
    @Get
    public Map<String, String> index() {
        return new HashMap<>();
    }

    @Produces(value = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Get("/excel") // <4>
    public SystemFile excel() { // <5>
        return bookExcelService.excelFileFromBooks(bookRepository.findAll());
    }
}
