package DataBuilder;
import DataClass.*;

import java.util.List;
import com.github.javafaker.Faker;

public class DataBuild {
    private static final Faker faker= new Faker();
    public static CartRequestDetails cart(){
        CartRequestDetails cart= CartRequestDetails.builder()
                        .Id(faker.number().numberBetween(1, 120))
                        .products(List.of(
                            ProductRequest.builder()
                                .id(faker.number().numberBetween(1, 100))
                                .quantity(faker.number().numberBetween(1, 2)).build()
                        )).build();
        return cart;
    }
}
