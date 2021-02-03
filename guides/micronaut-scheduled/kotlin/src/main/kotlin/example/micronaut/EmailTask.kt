package example.micronaut

class EmailTask(private val emailUseCase: EmailUseCase, private val email: String, private val message: String) : Runnable {

    override fun run() {
        emailUseCase.send(email, message)
    }

}
