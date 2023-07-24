package example.micronaut;

import io.micronaut.context.MessageSource;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.views.View;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import io.micronaut.http.annotation.Error;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static jakarta.validation.Path.*;

@Controller
class WebController {
    @View("results.html")
    @Get("/results")
    void results() {
    }

    @View("form.html")
    @Get
    Map<String, Object> index() {
        return model(new PersonForm(), Collections.emptyMap());
    }

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post
    HttpResponse<?> checkPersonInfo(@Body @Valid PersonForm personForm) {
        return HttpResponse.seeOther(URI.create("/results"));
    }

    @View("form.html")
    @Error(exception = ConstraintViolationException.class)
    Map<String, Object> onCheckPersonInfo(HttpRequest<?> request, ConstraintViolationException ex) {
        return model(request.getBody(PersonForm.class).orElseGet(PersonForm::new),
                violationsMessages(ex.getConstraintViolations()));
    }

    private Map<String, Object> model(PersonForm personForm, Map<String, String> fieldErrors) {
        Map<String, Object> model = new HashMap<>();
        model.put("personForm", personForm);
        model.put("fieldErrors", fieldErrors);
        return model;
    }

    private Map<String, String> violationsMessages(Set<ConstraintViolation<?>> violations) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (ConstraintViolation<?> violation : violations) {
            Node lastNode = lastNode(violation.getPropertyPath());
            fieldErrors.put(lastNode.getName(), violation.getMessage());
        }
        return fieldErrors;
    }

    private static Node lastNode(Path path) {
        Node lastNode = null;
        for (final Node node : path) {
            lastNode = node;
        }
        return lastNode;
    }
}