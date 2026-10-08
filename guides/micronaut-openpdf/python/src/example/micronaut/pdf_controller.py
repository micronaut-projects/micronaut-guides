from io import BytesIO

from micronaut.http import HttpResponse
from micronaut.http.annotation import Get
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from pdfme import PDF


@ExecuteOn(TaskExecutors.BLOCKING)  # <1>
@Get("/pdf/download", produces="application/pdf")  # <2>
def download() -> HttpResponse:  # <3>
    document = PDF()
    document.add_page()
    document.text("Hello World")
    with BytesIO() as output:
        document.output(output)
        return HttpResponse.ok(output.getvalue()).header(
            "Content-Disposition", "attachment; filename=example.pdf"
        )
