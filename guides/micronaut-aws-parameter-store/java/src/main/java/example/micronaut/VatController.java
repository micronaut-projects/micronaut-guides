package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@Controller // <1>
public class VatController {

    private final Vat vat;

    public VatController(Vat vat) {  // <2>
        this.vat = vat;
    }

    @Get("/vat")  // <3>
    public Map<String, BigDecimal> index() {
        return Collections.singletonMap("rate", vat.getRate());
    }
}
