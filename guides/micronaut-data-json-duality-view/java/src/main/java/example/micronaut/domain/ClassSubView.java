package example.micronaut.domain;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.JsonSubView;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@JsonSubView(entity = Class.class)
public class ClassSubView {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    private Long id;
}