package com.example;

import io.micronaut.context.LocalizedMessageSource;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.util.Optional;

@Controller
public class HelloWorldController {

    private final LocalizedMessageSource messageSource;

    public HelloWorldController(LocalizedMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Get
    Optional<String> index() {
        return messageSource.getMessage("hello.world");
    }
}
