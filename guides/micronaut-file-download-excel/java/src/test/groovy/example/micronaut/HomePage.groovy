package example.micronaut

import geb.Page

class HomePage extends Page {

    static at = { title == 'Micronaut' }

    static url = '/'

    static content = {
        excelLink { $('a', text: 'Excel', 0) }
    }

    void downloadExcel() {
        excelLink.click()
    }
}
