package example.micronaut

class MicronautguideSpec extends BaseMongoDataSpec {

    void 'test it works'() {
        expect:
        application.running
    }

}
