//tag::package[]
package example.micronaut
//end::package[]

//tag::imports[]
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.views.View
import javax.validation.ConstraintViolationException
import javax.validation.Valid

//end::imports[]

@Controller("/books")
open class BookController
    (
    private val messageSource: MessageSource // <1>
) {

    //tag::create[]
    @View("bookscreate") // <2>
    @Get("/create") // <3>
    open fun create(): Map<String, Any?> {
        return createModelWithBlankValues()
    }
    //end::create[]

    //tag::stock[]
    @Produces(MediaType.TEXT_PLAIN)
    @Get("/stock/{isbn}")
    open fun stock(isbn: String?): Int {
        throw OutOfStockException()
    }
    //end::stock[]

    //tag::save[]
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <4>
    @Post("/save") // <5>
    open fun save(@Valid @Body cmd: CommandBookSave?): HttpResponse<*> { // <6>
        return HttpResponse.ok<Any>()
    }
    //end::save[]

    //tag::onSavedFailed[]
    @View("bookscreate")
    @Error(exception = ConstraintViolationException::class) // <2>
    open fun onSavedFailed(request: HttpRequest<*>, ex: ConstraintViolationException): Map<String, Any?> { // <3>
        val model = createModelWithBlankValues()
        model["errors"] = messageSource.violationsMessages(ex.constraintViolations)
        val cmd = request.getBody(CommandBookSave::class.java)
        cmd.ifPresent { bookSave: CommandBookSave -> populateModel(model, bookSave) }
        return model
    }

    private fun populateModel(model: MutableMap<String, Any?>, bookSave: CommandBookSave) {
        model["title"] = bookSave.title
        model["pages"] = bookSave.pages
    }
    //end::onSavedFailed[]

    //tag::createModelWithBlankValues[]
    private fun createModelWithBlankValues(): MutableMap<String, Any?> {
        return mutableMapOf("title" to "", "pages" to "")
    } //end::createModelWithBlankValues[]
}