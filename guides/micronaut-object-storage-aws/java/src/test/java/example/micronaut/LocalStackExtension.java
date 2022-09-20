package example.micronaut;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class LocalStackExtension implements AfterAllCallback {
    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        LocalStack.close();
    }
}
