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
