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

    public static ReceipeResponseDetails receipe(){
        ReceipeResponseDetails receipe= ReceipeResponseDetails.builder()
                                        .id(faker.number().numberBetween(1, 100))
                                        .userId(faker.number().numberBetween(1, 40))
                                        .reviewCount(faker.number().numberBetween(1, 100))
                                        .name(faker.food().dish())
                                        .image(faker.internet().url())
                                        .ingredients(new String[]{faker.options().option("Pizza","Momos","Sweets")})
                                        .instructions(new String[]{faker.options().option("Add A","Add B")})
                                        .tags(new String[]{faker.options().option("India", "continental","Thai", "Chinese")})
                                        .mealType(new String[]{faker.options().option("Lunch", "diiner","snacks")})
                                        .prepTimeMinutes(faker.number().numberBetween(1, 13))
                                        .cookTimeMinutes(faker.number().numberBetween(1, 15))
                                        .servings(faker.number().numberBetween(25, 35))
                                        .caloriesPerServing(faker.number().numberBetween(1, 95))
                                        .difficulty(new String[]{faker.options().option("Easy","Medium", "Hard")})
                                        .cuisine(new String[]{faker.options().option("Indian","Chinese", "Continental")})
                                        .ratings(faker.number().randomDouble(2,2,99))
                                        .build();  
        return receipe;                                           
    }
}
