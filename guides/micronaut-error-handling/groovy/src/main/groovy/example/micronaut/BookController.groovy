//tag::package[]
package example.micronaut
//end::package[]

//tag::imports[]
import groovy.transform.CompileStatic
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
@CompileStatic
@Controller('/books') // <1>
class BookController {
//end::clazz[]

    //tag::di[]
    private final MessageSource messageSource

    BookController(MessageSource messageSource) { // <1>
        this.messageSource = messageSource
    }
    //end::di[]

    //tag::create[]
    @View('bookscreate') // <2>
    @Get('/create') // <3>
    Map<String, Object> create() {
        createModelWithBlankValues()
    }
    //end::create[]

    //tag::stock[]
    @Produces(MediaType.TEXT_PLAIN)
    @Get('/stock/{isbn}')
    Integer stock(String isbn) {
        throw new OutOfStockException()
    }
    //end::stock[]

    //tag::save[]
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <4>
    @Post('/save') // <5>
    HttpResponse save(@Valid @Body CommandBookSave cmd) { // <6>
        return HttpResponse.ok()
    }
    //end::save[]

    //tag::onSavedFailed[]
    @View('bookscreate')
    @Error(exception = ConstraintViolationException) // <2>
    Map<String, Object> onSavedFailed(HttpRequest request, ConstraintViolationException ex) { // <3>
        final Map<String, Object> model = createModelWithBlankValues()
        model.errors = messageSource.violationsMessages(ex.constraintViolations)
        Optional<CommandBookSave> cmd = request.getBody(CommandBookSave) as Optional
        cmd.ifPresent(bookSave -> populateModel(model, bookSave as CommandBookSave))
        model
    }

    private void populateModel(Map<String, Object> model, CommandBookSave bookSave) {
        model.title = bookSave.title
        model.pages = bookSave.pages
    }

    //end::onSavedFailed[]

    //tag::createModelWithBlankValues[]
    private Map<String, Object> createModelWithBlankValues() {
        [title: '', pages: ''] as Map
    }
    //end::createModelWithBlankValues[]
    //tag::endOfFile[]
}
//end::endOfFile[]