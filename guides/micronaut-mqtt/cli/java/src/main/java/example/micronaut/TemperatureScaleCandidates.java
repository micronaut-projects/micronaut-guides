package example.micronaut;

import java.util.ArrayList;

public class TemperatureScaleCandidates extends ArrayList<String> {

    public TemperatureScaleCandidates() {
        super(Scale.candidates());
    }
}
