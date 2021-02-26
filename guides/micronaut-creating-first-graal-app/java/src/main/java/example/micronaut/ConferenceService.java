package example.micronaut;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Singleton // <1>
public class ConferenceService {

    private static final List<Conference> CONFERENCES = Arrays.asList(
            new Conference("Greach"),
            new Conference("GR8Conf EU"),
            new Conference("Micronaut Summit"),
            new Conference("Devoxx Belgium"),
            new Conference("Oracle Code One"),
            new Conference("CommitConf"),
            new Conference("Codemotion Madrid")
    );

    public Conference randomConf() { // <2>
        return CONFERENCES.get(new Random().nextInt(CONFERENCES.size()));
    }
}
