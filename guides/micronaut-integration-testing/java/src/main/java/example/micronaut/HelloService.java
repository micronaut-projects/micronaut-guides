package example.micronaut;

import jakarta.inject.Singleton;

@Singleton
class HelloService {

    HelloRepository helloRepository;

    HelloService(HelloRepository helloRepository) { // <1>
        this.helloRepository = helloRepository;
    }

    public String sayHello(String language) {
        return helloRepository.findHelloByLanguage(language);
    }
}
