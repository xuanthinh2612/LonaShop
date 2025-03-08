package LonaShop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemUpdateResponse {

    private Long cartItemId;
    private Long quantity;
    private boolean success = true;
    private String successMessage;
    private String errorMessage;
    private boolean authError;
    private boolean productError;
}
