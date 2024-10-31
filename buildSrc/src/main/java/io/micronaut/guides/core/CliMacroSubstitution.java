package io.micronaut.guides.core;

import jakarta.inject.Singleton;
import org.gradle.api.GradleException;
import java.util.stream.Collectors;

@Singleton
public class CliMacroSubstitution extends PlaceholderWithTargetMacroSubstitution {
    private static final String CLI_MESSAGING = "create-messaging-app";
    private static final String CLI_DEFAULT = "create-app";
    private static final String CLI_GRPC = "create-grpc-app";
    private static final String CLI_FUNCTION = "create-function-app";
    private static final String CLI_CLI = "create-cli-app";

    @Override
    protected String getMacroName() {
        return "cli-command";
    }

    @Override
    protected String getSubstitution(Guide guide, GuidesOption option, String appName) {
        App app = guide.apps().stream()
                .filter(a -> a.name().equals(appName))
                .findFirst()
                .orElse(null);
        if (app != null) {
            return cliCommandForApp(app);
        } else{
            throw new GradleException("No CLI command found for app: " + app + " -- should be one of " + guide.apps().stream().map(el -> "@" + el + ":cli-command@").collect(Collectors.joining(", ")));
        }
    }

    private static String cliCommandForApp(App app) {
        return switch (app.applicationType()) {
            case CLI -> CLI_CLI;
            case FUNCTION -> CLI_FUNCTION;
            case GRPC -> CLI_GRPC;
            case MESSAGING -> CLI_MESSAGING;
            case DEFAULT -> CLI_DEFAULT;
            default -> throw new IllegalArgumentException("Unknown application type: " + app.applicationType());
        };
    }
}
