from micronaut.http import HttpHeaders, HttpRequest, HttpResponse, HttpStatus, MediaType
from micronaut.http.annotation import Controller, Delete, Get, Post, Status
from micronaut.http.multipart import CompletedFileUpload
from micronaut.http.server.types.files import StreamedFile
from micronaut.http.server.util import HttpHostResolver
from micronaut.http.uri import UriBuilder
from micronaut.objectstorage.aws import AwsS3ObjectStorageEntry, AwsS3Operations
from micronaut.objectstorage.request import UploadRequest
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from software.amazon.awssdk.services.s3.model import ObjectCannedACL


# tag::begin-class[]
@Controller("/pictures")  # <1>
@ExecuteOn(TaskExecutors.BLOCKING)  # <2>
class ProfilePicturesController:

    PREFIX = "/pictures"

    def __init__(
        self,
        object_storage: AwsS3Operations,  # <3>
        http_host_resolver: HttpHostResolver,  # <4>
    ):
        self.object_storage = object_storage
        self.http_host_resolver = http_host_resolver
# end::begin-class[]

    # tag::upload[]
    @Post(uri="/{userId}", consumes=MediaType.MULTIPART_FORM_DATA)
    def upload(
        self,
        fileUpload: CompletedFileUpload,
        userId: str,
        request: HttpRequest,
    ) -> HttpResponse:
        key = self.build_key(userId)  # <1>
        object_storage_upload = UploadRequest.fromCompletedFileUpload(fileUpload, key)  # <2>
        response = self.object_storage.upload(  # <3>
            object_storage_upload,
            lambda builder: builder.acl(ObjectCannedACL.PUBLIC_READ),  # <4>
        )

        return (
            HttpResponse.created(self.location(request, userId))  # <5>
            .header(HttpHeaders.ETAG, response.getETag())  # <6>
        )

    @staticmethod
    def build_key(userId: str) -> str:
        return f"{userId}.jpg"

    def location(self, request: HttpRequest, userId: str):
        return (
            UriBuilder.of(self.http_host_resolver.resolve(request))
            .path(self.PREFIX)
            .path(userId)
            .build()
        )
    # end::upload[]

    # tag::download[]
    @Get("/{userId}")
    def download(self, userId: str) -> HttpResponse[StreamedFile]:
        key = self.build_key(userId)
        entry = self.object_storage.retrieve(key)  # <1>
        if entry.isEmpty():
            return HttpResponse.notFound()
        return self.build_streamed_file(entry.get())  # <2>

    @staticmethod
    def build_streamed_file(entry: AwsS3ObjectStorageEntry) -> HttpResponse[StreamedFile]:
        native_entry = entry.getNativeEntry()
        media_type = MediaType.of(native_entry.contentType())
        file = StreamedFile(entry.getInputStream(), media_type).attach(entry.getKey())
        http_response = HttpResponse.ok().header(HttpHeaders.ETAG, native_entry.eTag())  # <3>
        file.process(http_response)
        return http_response.body(file)
    # end::download[]

    # tag::delete[]
    @Status(HttpStatus.NO_CONTENT)
    @Delete("/{userId}")
    def delete(self, userId: str) -> None:
        key = self.build_key(userId)
        self.object_storage.delete(key)
    # end::delete[]

# tag::end-class[]
# end::end-class[]
