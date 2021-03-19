package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.math.BigDecimal;

@Controller // <1>
public class VatController {

    private final Vat vat;

    public VatController(Vat vat) {  // <2>
        this.vat = vat;
    }

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get("/vat")  // <4>
    public BigDecimal index() {
        return vat.getRate();
    }
}
