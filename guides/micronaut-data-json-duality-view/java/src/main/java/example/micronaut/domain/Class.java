package example.micronaut.domain;

import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;

@MappedEntity(value = "TBL_CLASS", alias = "c")
public class Class {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    private Long id;
}