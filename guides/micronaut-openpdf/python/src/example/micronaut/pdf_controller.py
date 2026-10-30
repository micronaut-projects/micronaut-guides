from io import BytesIO

from micronaut.http import HttpResponse
from micronaut.http.annotation import Get
from pdfme import PDF


@Get("/pdf/download", produces="application/pdf")  # <1> <2>
def download() -> HttpResponse:  # <3>
    document = PDF()
    document.add_page()
    document.text("Hello World")
    with BytesIO() as output:
        document.output(output)
        return HttpResponse.ok(output.getvalue()).header(  # <4>
            "Content-Disposition", "attachment; filename=example.pdf"
        )
