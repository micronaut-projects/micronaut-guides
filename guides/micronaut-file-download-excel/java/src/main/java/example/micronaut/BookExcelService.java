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

import java.util.List;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.http.server.types.files.SystemFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@DefaultImplementation(BookExcelServiceImpl.class)
public interface BookExcelService {
    String SHEET_NAME = "Books";
    String HEADER_ISBN = "Isbn";
    String HEADER_NAME = "Name";
    String HEADER_EXCEL_FILE_SUFIX = ".xlsx";
    String HEADER_EXCEL_FILE_PREFIX = "books";
    String HEADER_EXCEL_FILENAME = HEADER_EXCEL_FILE_PREFIX + HEADER_EXCEL_FILE_SUFIX;

    @NonNull
    SystemFile excelFileFromBooks(@NonNull @NotNull List<@Valid Book> bookList); // <1>
}
