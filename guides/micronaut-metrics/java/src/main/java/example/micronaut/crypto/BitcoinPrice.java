package example.micronaut.crypto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable // <1>
public class BitcoinPrice {

    private final Data data;

    public BitcoinPrice(Data data) {
        this.data = data;
    }

    public float getPrice() {
        return data.getPrice();
    }

    @Serdeable // <1>
    public static class Data {

        private final float price;

        public Data(float price) {
            this.price = price;
        }

        public float getPrice() {
            return price;
        }
    }
}
