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

import com.lowagie.text.Document
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfWriter
import io.micronaut.core.annotation.Nullable
import io.micronaut.core.io.Writable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

import java.nio.charset.Charset

@Controller('/pdf') // <1>
class PDFController {

    @ExecuteOn(TaskExecutors.BLOCKING) // <2>
    @Get('/download') // <3>
    HttpResponse<Writable> download() throws IOException { // <4>
        downloadPdf('example.pdf')
    }

    private HttpResponse<Writable> downloadPdf(String filename) throws IOException {
        HttpResponse.ok(pdfWritable())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF)
    }

    private Writable pdfWritable() {
        new Writable() {
            @Override
            void writeTo(OutputStream outputStream, @Nullable Charset charset) throws IOException {
                generatePDF(outputStream)
            }

            @Override
            void writeTo(Writer out) {
            }
        }
    }

    private void generatePDF(OutputStream outputStream) {
        Document document = new Document()
        try {
            PdfWriter.getInstance(document, outputStream)
            document.open()
            document.newPage()
            document.add(new Paragraph('Hello World'))
        } finally {
            document.close()
        }
    }
}
