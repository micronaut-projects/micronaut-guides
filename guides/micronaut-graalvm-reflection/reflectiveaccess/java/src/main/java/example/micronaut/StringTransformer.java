package example.micronaut;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Singleton // <1>
public class StringTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(StringTransformer.class);

    String transform(String input, String className, String methodName) {
        try {
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getDeclaredMethod(methodName, String.class);
            return method.invoke(null, input).toString();
        } catch (ClassNotFoundException e) {
            LOG.error("Class not found: {}", className);
            return input;
        } catch (NoSuchMethodException e) {
            LOG.error("Method not found: {}", methodName);
            return input;
        } catch (InvocationTargetException e) {
            LOG.error("InvocationTargetException: {}", e.getMessage());
            return input;
        } catch (IllegalAccessException e) {
            LOG.error("IllegalAccessException: {}", e.getMessage());
            return input;
        }
    }
}
