package example.micronaut

import groovy.transform.CompileStatic

@CompileStatic
class EmailTask implements Runnable {

    String email
    String message
    EmailUseCase emailUseCase

    @Override
    void run() {
        emailUseCase.send(email, message)
    }
}
