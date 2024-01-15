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

import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Controller("/pdf") // <1>
class PDFController {

    @ExecuteOn(TaskExecutors.BLOCKING) // <2>
    @Get("/download") // <3>
    HttpResponse<Writable> download() throws IOException { // <4>
        return download("example.pdf");
    }

    private HttpResponse<Writable> download(String filename) throws IOException {
        return HttpResponse.ok(pdfWritable())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF);
    }

    private Writable pdfWritable() {
        return new Writable() {
            public void writeTo(OutputStream outputStream, @Nullable Charset charset) throws IOException {
                generatePDF(outputStream);
            }

            public void writeTo(Writer out) {
            }
        };
    }

    private void generatePDF(OutputStream outputStream) {
        try (Document document = new Document()) {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.newPage();
            document.add(new Paragraph("Hello World"));
        }
    }
}
