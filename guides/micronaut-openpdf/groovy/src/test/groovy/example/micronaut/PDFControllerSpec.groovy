/*
 * Copyright 2017-2026 original authors
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

import com.lowagie.text.pdf.PdfReader
import com.lowagie.text.pdf.parser.PdfTextExtractor
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Shared
import spock.lang.Specification

@MicronautTest // <1>
class PDFControllerSpec extends Specification {

    @Shared
    @Inject
    @Client('/')
    HttpClient httpClient // <2>

    void "test download"() throws IOException {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        HttpResponse<byte[]> pdfResponse = client.exchange('/pdf/download', byte[])

        then:
        noExceptionThrown()
        pdfResponse.status() == HttpStatus.OK
        pdfResponse.headers.get(HttpHeaders.CONTENT_TYPE) == MediaType.APPLICATION_PDF
        pdfResponse.headers.get(HttpHeaders.CONTENT_DISPOSITION) == 'attachment; filename=example.pdf'
        textAtPage(pdfResponse.body(), 1) == 'Hello World'
    }

    private static String textAtPage(PdfReader pdfReader, int pageNumber) throws IOException {
        new PdfTextExtractor(pdfReader).getTextFromPage(pageNumber)
    }

    private static String textAtPage(byte[] pdfBytes, int pageNumber) throws IOException {
        PdfReader pdfReader = new PdfReader(pdfBytes)
        try {
            textAtPage(pdfReader, pageNumber)
        } finally {
            pdfReader.close()
        }
    }
}
