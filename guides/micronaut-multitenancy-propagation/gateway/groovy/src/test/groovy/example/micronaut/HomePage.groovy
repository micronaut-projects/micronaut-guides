package example.micronaut

import geb.Page

class HomePage extends Page {

    static url = "/"

    static at = { title == "Home" }

    static content = {
        li(required: false) { $('li') }
    }

    List<String> books() {
        li.collect { it.text() }
    }

    int numberOfBooks() {
        if (li.empty) {
           return 0
        }
        li.size()
    }
}
