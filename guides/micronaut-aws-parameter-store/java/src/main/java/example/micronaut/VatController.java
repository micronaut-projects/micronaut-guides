package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.math.BigDecimal;

@Controller
public class VatController {

    private final Vat vat;

    public VatController(Vat vat) {
        this.vat = vat;
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/vat")
    public BigDecimal index() {
        return vat.getRate();
    }
}
