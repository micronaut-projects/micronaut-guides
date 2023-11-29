package example.micronaut.controllers;

import example.micronaut.RegisterService;
import example.micronaut.exceptions.UserAlreadyExistsException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.View;
import io.micronaut.views.fields.FormGenerator;
import io.micronaut.views.fields.messages.Message;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Secured(SecurityRule.IS_ANONYMOUS) // <1>
@Controller(UserController.PATH) // <2>
public class UserController {

    public static final String PATH = "/user";
    private static final String PATH_AUTH = "/auth";
    private static final String PATH_AUTH_FAILED = "/authFailed";
    private static final String PATH_SIGN_UP = "/signUp";
    private final static String MODEL_KEY_FORM = "form";
    private static final String MODEL_KEY_ERROR = "error";
    private static final String PATH_SIGNUP = "/user/signUp";
    private static final String VIEW_SIGNUP = "/user/signup.html";
    private static final String VIEW_AUTH = "/user/auth.html";
    private static final String PATH_LOGIN = "/login";
    private static final Message MESSAGE_LOGIN_FAILED =
            Message.of("That username or password is incorrect. Please try again.", "login.failed");
    private static final Message MESSAGE_SIGNUP_FAILED =
            Message.of("Sorry, someone already has that username.", "signup.failed");
    private final FormGenerator formGenerator;
    private final RegisterService registerService;
    private final URI uriAuth;
    private final Map<String, Object> authModel;
    private final Map<String, Object> signUpModel;
    private final Map<String, Object> signUpFailedModel;
    private final Map<String, Object> authFailedModel;

    public UserController(FormGenerator formGenerator, RegisterService registerService) { // <3>
        this.formGenerator = formGenerator;
        this.registerService = registerService;
        this.uriAuth = UriBuilder.of(PATH).path(PATH_AUTH).build();
        this.signUpModel = Collections.singletonMap(MODEL_KEY_FORM,
                formGenerator.generate(PATH_SIGNUP, SignUpForm.class));
        this.signUpFailedModel = Map.of(MODEL_KEY_FORM,
                formGenerator.generate(PATH_SIGNUP, SignUpForm.class), MODEL_KEY_ERROR, MESSAGE_SIGNUP_FAILED);
        this.authModel = Collections.singletonMap(MODEL_KEY_FORM,
                formGenerator.generate(PATH_LOGIN, LoginForm.class));
        Map<String, Object> model = new HashMap<>(auth());
        model.put(MODEL_KEY_ERROR, MESSAGE_LOGIN_FAILED);
        this.authFailedModel = model;
    }

    @Produces(MediaType.TEXT_HTML) // <4>
    @Get(PATH_AUTH) // <5>
    @View(VIEW_AUTH) // <6>
    public Map<String, Object> auth() {
        return authModel;
    }

    @Produces(MediaType.TEXT_HTML) // <4>
    @Get(PATH_AUTH_FAILED) // <7>
    @View(VIEW_AUTH) // <6>
    public Map<String, Object> authFailed() {
        return authFailedModel;
    }

    @ExecuteOn(TaskExecutors.BLOCKING) // <8>
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <9>
    @Produces(MediaType.TEXT_HTML) // <4>
    @Post(PATH_SIGN_UP) // <10>
    public HttpResponse<?> signUpSave(@NotNull @Valid @Body SignUpForm signUpForm) {
        try {
            registerService.register(signUpForm.username(), signUpForm.password());
        } catch (UserAlreadyExistsException e) {
            return HttpResponse.unprocessableEntity().body(new ModelAndView<>(VIEW_SIGNUP, signUpFailedModel));
        }
        return HttpResponse.seeOther(uriAuth);
    }

    @Produces(MediaType.TEXT_HTML) // <4>
    @Get(PATH_SIGN_UP) // <11>
    @View(VIEW_SIGNUP) // <6>
    public Map<String, Object> signUp() {
        return signUpModel;
    }

    @Error(exception = ConstraintViolationException.class) // <12>
    public HttpResponse<?> onConstraintVioliationException(HttpRequest<?> request, ConstraintViolationException ex) { //<13>
        if (request.getPath().equals(PATH_SIGNUP)) {
            return request.getBody(SignUpForm.class)
                    .map(signUpForm -> HttpResponse.ok()
                            .body(new ModelAndView<>(VIEW_SIGNUP,
                                    Collections.singletonMap(MODEL_KEY_FORM,
                                            formGenerator.generate(PATH_SIGNUP, signUpForm, ex)))))
                    .orElseGet(HttpResponse::serverError);
        }
        return HttpResponse.serverError();
    }
}