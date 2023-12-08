package example.micronaut;

import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.core.convert.format.MapFormat;

import java.util.List;
import java.util.Map;

@EachProperty("micronaut.gateway.routes")
public class RouteConfiguration implements Route {

    private final String name;
    private String uri;

    private int order = 0;

    private Map<String, Object> predicates;

    public RouteConfiguration(@Parameter String name) {
        this.name = name;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public List<? extends Predicate> getPredicates() {
        Object path = predicates.get("path");
        return List.of((ConfigurationPredicate) () -> path != null ? path.toString() : null);
    }

    public void setPredicates(@MapFormat(transformation = MapFormat.MapTransformation.FLAT) Map<String, Object> predicates) {
        this.predicates = predicates;
    }



}
