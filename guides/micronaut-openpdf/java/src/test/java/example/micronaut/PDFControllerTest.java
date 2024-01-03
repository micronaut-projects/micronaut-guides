package example.micronaut;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest // <1>
class PDFControllerTest {
    @Test
    void testDownload(@Client("/") HttpClient httpClient) throws IOException { // <2>
        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<byte[]> pdfResponse = assertDoesNotThrow(() -> client.exchange("/pdf/download", byte[].class));
        assertEquals(HttpStatus.OK, pdfResponse.status());
        assertEquals(MediaType.APPLICATION_PDF, pdfResponse.getHeaders().get(HttpHeaders.CONTENT_TYPE));
        assertEquals("attachment; filename=example.pdf", pdfResponse.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION));
        byte[] pdfBytes = pdfResponse.body();
        String firstPageText = textAtPage(pdfBytes, 1);
        assertEquals("Hello World", firstPageText);
    }

    private static String textAtPage(PdfReader pdfReader, int pageNumber) throws IOException {
        PdfTextExtractor pdfTextExtractor = new PdfTextExtractor(pdfReader);
        return pdfTextExtractor.getTextFromPage(1);
    }

    private static String textAtPage(byte[] pdfBytes, int pageNumber) throws IOException {
        try (PdfReader pdfReader = new PdfReader(pdfBytes)) {
            return textAtPage(pdfReader, pageNumber);
        }
    }
}
