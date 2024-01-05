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
package example.micronaut

import geb.Page
import geb.error.RequiredPageContentNotPresent
import geb.module.TextInput

class BookCreatePage extends Page {

    static url = '/books/create'

    static at = { title == 'Create Book' }

    static content = {
        titleInput { $('input', name: 'title').module(TextInput) }
        pagesInput { $('input', name: 'pages').module(TextInput) }
        saveButton { $('input', type: 'submit') }
        errorsLi(required: false) { $('#errors li') }
    }

    String title() {
        titleInput.value()
    }

    String pages() {
        pagesInput.value()
    }

    void save(String title, int pages) {
        titleInput.text = title
        pagesInput.text = pages as String
        saveButton.click()
    }

    boolean hasErrors() {
        !errorsLi.empty
    }

    List<String> errors() {
        if (errorsLi.empty) {
           return [] as List<String>
        }
        errorsLi.collect { it.text() }
    }
}
