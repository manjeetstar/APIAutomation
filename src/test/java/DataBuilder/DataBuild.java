package DataBuilder;
import DataClass.*;

import java.util.List;
import com.github.javafaker.Faker;

public class DataBuild {
    private static final Faker faker= new Faker();
    public static CartDetails cart(){
        CartDetails cart= CartDetails.builder()
                        .userId(faker.number().numberBetween(1, 120))
                        .products(List.of(
                            Product.builder()
                                .id(faker.number().numberBetween(1, 100))
                                .quantity(faker.number().numberBetween(1, 2)).build()
                        )).build();
        return cart;
    }
}
