package example.micronaut

import geb.Page

class TenantPage extends Page {

    static url = "/tenant"

    static at = { title == "Tenants" }

    static content = {
        link { $('a', text: it) }
    }

    void select(String text) {
        link(text).click()
    }
}
