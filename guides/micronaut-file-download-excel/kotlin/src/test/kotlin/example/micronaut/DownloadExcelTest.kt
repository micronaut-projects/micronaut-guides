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
package example.micronaut

import builders.dsl.spreadsheet.query.api.CellCriterion
import builders.dsl.spreadsheet.query.api.RowCriterion
import builders.dsl.spreadsheet.query.api.SheetCriterion
import builders.dsl.spreadsheet.query.api.WorkbookCriterion
import builders.dsl.spreadsheet.query.poi.PoiSpreadsheetCriteria
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.FileNotFoundException
import java.io.InputStream

@MicronautTest // <1>
class DownloadExcelTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    @Throws(FileNotFoundException::class)
    fun booksCanBeDownloadedAsAnExcelFile() {
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/excel")
                .accept("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") // <3>
        val response = client.toBlocking().exchange(request, ByteArray::class.java)

        assertEquals(HttpStatus.OK, response.status)

        val inputStream: InputStream = ByteArrayInputStream(response.body()) // <4>
        val query = PoiSpreadsheetCriteria.FACTORY.forStream(inputStream)
        val result = query.query { workbookCriterion: WorkbookCriterion ->
            workbookCriterion.sheet(BookExcelService.SHEET_NAME) { sheetCriterion: SheetCriterion ->
                sheetCriterion.row { rowCriterion: RowCriterion ->
                    rowCriterion.cell { cellCriterion: CellCriterion -> cellCriterion.value("Building Microservices") }
                }
            }
        }

        assertEquals(1, result.cells.size)
    }
}
