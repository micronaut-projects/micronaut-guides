from typing import Protocol

from micronaut.http import HttpRequest, HttpResponse, HttpStatus, MediaType
from micronaut.http.annotation import Delete, Get, Post, Status
from micronaut.http.multipart import CompletedFileUpload
from micronaut.http.server.types.files import StreamedFile


# tag::class[]
class ProfilePicturesApi(Protocol):

    @Post(uri="/{userId}", consumes=MediaType.MULTIPART_FORM_DATA)  # <1>
    def upload(
        self,
        fileUpload: CompletedFileUpload,
        userId: str,
        request: HttpRequest,
    ) -> HttpResponse:
        ...

    @Get("/{userId}")  # <2>
    def download(self, userId: str) -> HttpResponse[StreamedFile]:
        ...

    @Status(HttpStatus.NO_CONTENT)  # <3>
    @Delete("/{userId}")  # <4>
    def delete(self, userId: str) -> None:
        ...
# end::class[]
