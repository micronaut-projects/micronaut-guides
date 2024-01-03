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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        return pdfTextExtractor.getTextFromPage(pageNumber);
    }

    private static String textAtPage(byte[] pdfBytes, int pageNumber) throws IOException {
        try (PdfReader pdfReader = new PdfReader(pdfBytes)) {
            return textAtPage(pdfReader, pageNumber);
        }
    }
}
