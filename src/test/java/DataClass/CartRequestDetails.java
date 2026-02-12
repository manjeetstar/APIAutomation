package DataClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

@Data
@Builder
@AllArgsConstructor
public class CartRequestDetails {
    @JsonAlias({"userId" , "USERID"})
    private int Id;
    private List<ProductRequest> products;
}
