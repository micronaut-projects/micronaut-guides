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
package example.micronaut

import io.micronaut.context.annotation.DefaultImplementation
import io.micronaut.http.server.types.files.SystemFile

@DefaultImplementation(BookExcelServiceImpl::class)
interface BookExcelService {

    fun excelFileFromBooks(bookList: List<Book>): SystemFile // <1>

    companion object {
        const val SHEET_NAME = "Books"
        const val HEADER_ISBN = "Isbn"
        const val HEADER_NAME = "Name"
        const val HEADER_EXCEL_FILE_SUFIX = ".xlsx"
        const val HEADER_EXCEL_FILE_PREFIX = "books"
        const val HEADER_EXCEL_FILENAME = HEADER_EXCEL_FILE_PREFIX + HEADER_EXCEL_FILE_SUFIX
    }
}
