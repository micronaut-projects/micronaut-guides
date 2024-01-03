package example.micronaut;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Controller("/pdf") // <1>
class PDFController {
    @ExecuteOn(TaskExecutors.BLOCKING) // <2>
    @Get("/download") // <3>
    HttpResponse<byte[]> download() throws IOException {
        return download("example.pdf");
    }

    private HttpResponse<byte[]> download(String filename) throws IOException {
        return HttpResponse.ok(pdfBytes())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF);
    }

    private byte[] pdfBytes() throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            generatePDF(outputStream);
            return outputStream.toByteArray();
        }
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
