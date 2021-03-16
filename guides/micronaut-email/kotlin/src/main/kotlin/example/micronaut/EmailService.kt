package example.micronaut

interface EmailService {
    fun send(email: Email)
}