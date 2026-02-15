package DataClass;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceipeResponseDetails {
    private int id, userId, reviewCount ;
    public String name, image ;
    public String ingredients[], instructions[], tags[], mealType[];
    public int prepTimeMinutes, cookTimeMinutes, servings, caloriesPerServing ;   
    public String difficulty[], cuisine[];
    public double ratings;   
}
