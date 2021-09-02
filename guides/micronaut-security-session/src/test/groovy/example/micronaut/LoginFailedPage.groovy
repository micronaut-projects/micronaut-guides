package example.micronaut

import geb.Page

class LoginFailedPage extends Page {

    static at = { title == 'Login Failed' }

    static url = '/login/authFailed'
}
