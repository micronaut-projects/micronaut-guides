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
package example.micronaut.model;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import java.security.Principal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Serdeable
public class ViewModel {

    private final String pageTitle;
    private final String pageClass;
    @Nullable
    private final String action;
    @Nullable
    private final Principal principal;

    public ViewModel(String pageTitle) {
        this(pageTitle, "", null, null);
    }

    public ViewModel(String pageTitle, String pageClass) {
        this(pageTitle, pageClass, null, null);
    }

    public ViewModel(String pageTitle, String pageClass, String action) {
        this(pageTitle, pageClass, action, null);
    }

    public ViewModel(String pageTitle, String pageClass, @Nullable String action, @Nullable Principal principal) {
        this.pageTitle = pageTitle;
        this.pageClass = pageClass;
        this.action = action;
        this.principal = principal;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getPageClass() {
        return pageClass;
    }

    @Nullable
    public String getAction() {
        return action;
    }

    @Nullable
    public Principal getPrincipal() {
        return principal;
    }

    public String getRenderDate() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}