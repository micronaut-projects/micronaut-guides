package io.micronaut.guides.core;

import com.fizzed.rocker.Rocker;
import groovy.lang.Singleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class DefaultEnvironmentVariablesMacroRenderer implements EnvironmentVariablesMacroRenderer {

    @Override
    public List<String> render(List<String[]> environment) {
        String rendered = Rocker.template("io/micronaut/guides/core/template/environmentVariables.rocker.html",environment).render().toString();
        String[] lines = rendered.split("\\r?\\n|\\r");
        return new ArrayList<>(Arrays.asList(lines));
    }
}
