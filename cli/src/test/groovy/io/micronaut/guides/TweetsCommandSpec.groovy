package io.micronaut.guides

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class TweetsCommandSpec extends Specification {
    @Shared @AutoCleanup ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

    void "output prints lists of tweets"() {
        given:
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        System.setOut(new PrintStream(baos))

        String[] args = [] as String[]
        PicocliRunner.run(TweetsCommand, ctx, args)

        expect:
        baos.toString().contains('Creating your first Micronaut')
    }
}
