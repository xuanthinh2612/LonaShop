package LonaShop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemUpdateRequest {

    private Long cartItemId;
    private Long quantity;
    private Long productId;
}
