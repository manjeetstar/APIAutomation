package DataClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CartRequestDetails {
    private int userId;
    private List<ProductRequest> products;
}
