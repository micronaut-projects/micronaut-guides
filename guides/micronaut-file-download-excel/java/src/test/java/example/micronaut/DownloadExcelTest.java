package example.micronaut;

import builders.dsl.spreadsheet.query.api.SpreadsheetCriteria;
import builders.dsl.spreadsheet.query.api.SpreadsheetCriteriaResult;
import builders.dsl.spreadsheet.query.poi.PoiSpreadsheetCriteria;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
class DownloadExcelTest {

    @Inject
    @Client("/")
    HttpClient client; // <2>

    @Test
    public void booksCanBeDownloadedAsAnExcelFile() throws FileNotFoundException {
        HttpRequest<?> request = HttpRequest.GET("/excel")
                .accept("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // <3>
        HttpResponse<byte[]> response = client.toBlocking().exchange(request, byte[].class);

        assertEquals(HttpStatus.OK, response.getStatus());

        InputStream inputStream = new ByteArrayInputStream(response.body()); // <4>
        SpreadsheetCriteria query = PoiSpreadsheetCriteria.FACTORY.forStream(inputStream);
        SpreadsheetCriteriaResult result = query.query(workbookCriterion -> {
            workbookCriterion.sheet(BookExcelService.SHEET_NAME, sheetCriterion ->
                    sheetCriterion.row(rowCriterion ->
                            rowCriterion.cell(cellCriterion -> cellCriterion.value("Building Microservices"))));
        });
        assertEquals(result.getCells().size(), 1);
    }
}
