package example.micronaut

import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteriaResult
import builders.dsl.spreadsheet.query.poi.PoiSpreadsheetCriteria
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import spock.lang.Specification
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import jakarta.inject.Inject
import io.micronaut.http.HttpResponse

@MicronautTest // <1>
class DownloadExcelSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client // <2>

    def "books can be downloaded as an excel file"() {
        when:
        HttpResponse<byte[]> response = client.toBlocking().exchange(HttpRequest.GET('/excel')  // <3>
                .accept("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), byte[])

        then:
        noExceptionThrown()
        response.status == HttpStatus.OK

        when:
        InputStream inputStream = new ByteArrayInputStream(response.body())  // <4>
        SpreadsheetCriteria query = PoiSpreadsheetCriteria.FACTORY.forStream(inputStream)
        SpreadsheetCriteriaResult result = query.query( { workbookCriterion ->
            workbookCriterion.sheet(BookExcelService.SHEET_NAME, { sheetCriterion ->
                sheetCriterion.row({ rowCriterion ->
                    rowCriterion.cell({ cellCriterion ->
                        cellCriterion.value('Building Microservices')
                    })
                })
            })
        })

        then: 'a row is found'
        result.cells.size() == 1
    }
}
