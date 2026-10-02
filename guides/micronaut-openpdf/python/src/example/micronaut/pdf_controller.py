from io import BytesIO

from micronaut.http import HttpHeaders, HttpResponse, MediaType
from micronaut.http.annotation import Controller, Get
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from pdfme import PDF


@Controller("/pdf")  # <1>
class PDFController:

    @ExecuteOn(TaskExecutors.BLOCKING)  # <2>
    @Get("/download")  # <3>
    def download(self) -> HttpResponse:  # <4>
        return self._download("example.pdf")

    def _download(self, filename: str) -> HttpResponse:
        return (
            HttpResponse.ok(self._pdf_bytes())
            .header(HttpHeaders.CONTENT_DISPOSITION, f"attachment; filename={filename}")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF)
        )

    @staticmethod
    def _pdf_bytes() -> bytes:
        document = PDF()
        document.add_page()
        document.text("Hello World")
        with BytesIO() as output:
            document.output(output)
            return output.getvalue()
