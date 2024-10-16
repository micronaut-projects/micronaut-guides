package io.micronaut.guides.core;

import java.util.List;

public interface EnvironmentVariablesMacroRenderer {
    List<String> render(List<String[]> environment);
}
