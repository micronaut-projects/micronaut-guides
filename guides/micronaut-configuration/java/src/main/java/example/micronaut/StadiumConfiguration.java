package example.micronaut;


import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.core.annotation.Introspected;

@Introspected
@EachProperty("stadium") // <1>
public class StadiumConfiguration {
    private String name; // <2>
    private String city;
    private Integer size;

    public StadiumConfiguration(@Parameter String name) { // <2>
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
