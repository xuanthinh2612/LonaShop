package LonaShop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemUpdateResponse {

    private Long cartItemId;
    private Long quantity;
    private Boolean ok = true;
}
