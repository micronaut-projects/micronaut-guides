package example.micronaut
/*
//tag::package[]
package example.micronaut
//end::package[]
*/

//tag::imports[]
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.views.View
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Valid
//end::imports[]

//tag::clazz[]
@Controller("/books") // <1>
open class BookController {
//end::clazz[]

    //tag::di[]
    private var messageSource: MessageSource
    constructor(messageSource: MessageSource) { // <1>
        this.messageSource = messageSource
    }
    //end::di[]

    //tag::create[]
    @View("bookscreate") // <2>
    @Get("/create") // <3>
    fun create(): Map<String, Any> {
        return createModelWithBlankValues()
    }
    //end::create[]

    //tag::stock[]
    @Produces(MediaType.TEXT_PLAIN)
    @Get("/stock/{isbn}")
    fun stock(isbn: String): Int {
        throw OutOfStockException()
    }
    //end::stock[]

    //tag::save[]
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <4>
    @Post("/save") // <5>
    open fun save(@Valid @Body cmd: CommandBookSave): HttpResponse<Any> { // <6>
        return HttpResponse.ok()
    }
    //end::save[]

    //tag::onSavedFailed[]
    @View("bookscreate")
    @Error(exception = ConstraintViolationException::class) // <2>
    fun onSaveFailed(request: HttpRequest<Any>, ex: ConstraintViolationException): Map<String, Any> { // <3>
        val model: MutableMap<String, Any> = mutableMapOf("errors" to messageSource.violationsMessages(ex.constraintViolations))
        val cmd = request.getBody(CommandBookSave::class.java)
        cmd.ifPresentOrElse({ model.putAll(mapOf(
            "title" to it.title, "pages" to it.pages
        )) }, {
            model.putAll(createModelWithBlankValues())
        })
        return model
    }
    //end::onSavedFailed[]

    //tag::createModelWithBlankValues[]
    private fun createModelWithBlankValues(): Map<String, Any> {
        return mapOf("title" to "", "pages" to "")
    }
    //end::createModelWithBlankValues[]

//tag::endOfFile[]
}
//end::endOfFile[]