package DataClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CartDetails {
    private int userId;
    private List<Product> products;
}
