package DataClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class CartResponseDetails {
    private int id;
    private List<ProductResponse> products;
    private double total;
    private int discountedTotal;
    private int userId;
    private int totalProducts;
    private int totalQuantity;    
}
