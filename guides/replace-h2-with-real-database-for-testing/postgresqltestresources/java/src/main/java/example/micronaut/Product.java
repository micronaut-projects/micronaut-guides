package example.micronaut;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // <1>
@Table(name = "products") // <2>
public class Product {

    @Id // <3>
    private Long id;

    @Column(nullable = false, unique = true)  // <4>
    private String code;

    @Column(nullable = false) // <4>
    private String name;

    public Product() {}

    public Product(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
