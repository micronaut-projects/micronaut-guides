package example.micronaut

import groovy.transform.CompileStatic

@CompileStatic
class TemperatureScaleCandidates extends ArrayList<String> {

    TemperatureScaleCandidates() {
        super(Scale.candidates());
    }
}
