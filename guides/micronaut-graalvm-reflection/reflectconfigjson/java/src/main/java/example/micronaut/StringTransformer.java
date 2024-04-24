/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
