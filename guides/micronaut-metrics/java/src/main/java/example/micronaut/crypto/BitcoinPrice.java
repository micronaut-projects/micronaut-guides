package example.micronaut.crypto;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class BitcoinPrice {

    private final Data data;

    public BitcoinPrice(Data data) {
        this.data = data;
    }

    public float getPrice() {
        return data.getPrice();
    }

    @Introspected
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
