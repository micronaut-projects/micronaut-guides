/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.endpoints.LogoutControllerConfiguration;
import io.micronaut.security.utils.SecurityService;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.fields.Fieldset;
import io.micronaut.views.fields.Form;
import io.micronaut.views.fields.FormGenerator;
import io.micronaut.views.fields.elements.InputSubmitFormElement;
import io.micronaut.views.fields.messages.Message;
import io.micronaut.views.model.ViewModelProcessor;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton // <1>
class LogoutFormViewModelProcessor  implements ViewModelProcessor<Map<String, Object>> { // <2>

    private static final String MODEL_KEY = "logoutForm";

    private final SecurityService securityService;
    private final Form logoutForm;

    LogoutFormViewModelProcessor(FormGenerator formGenerator, // <3>
                                 SecurityService securityService,
                                 LogoutControllerConfiguration logoutControllerConfiguration) {
        this.securityService = securityService;
        this.logoutForm = formGenerator.generate(logoutControllerConfiguration.getPath(),
                new Fieldset(Collections.emptyList(), Collections.emptyList()), InputSubmitFormElement
                        .builder()
                        .value(Message.of("Logout", "logout.submit"))
                        .build());
    }

    @Override
    public void process(@NonNull HttpRequest<?> request, @NonNull ModelAndView<Map<String, Object>> modelAndView) {
        if (securityService.isAuthenticated()) {
            Map<String, Object> viewModel = modelAndView.getModel().orElseGet(() -> {
                final HashMap<String, Object> newModel = new HashMap<>(1);
                modelAndView.setModel(newModel);
                return newModel;
            });
            try {
                viewModel.putIfAbsent(MODEL_KEY, logoutForm);
            } catch (UnsupportedOperationException ex) {
                final HashMap<String, Object> modifiableModel = new HashMap<>(viewModel);
                modifiableModel.putIfAbsent(MODEL_KEY, logoutForm);
                modelAndView.setModel(modifiableModel);
            }
        }
    }
}
