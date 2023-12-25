package LonaShop.common;

public class CommonConst {

    //    cover status
    public static final int FLAG_ON = 1;
    public static final int FLAG_OFF = 0;


    // order status
    public static final String ORDER_STATUS = "orderStatus";
    public static final int ORDERED = 1;
    public static final int CONFIRMED = 2;
    public static final int PACKING = 3;
    public static final int DELIVERED = 4;
    public static final int USER_CANCELED = 5;
    public static final int DENIED = 6;

    // page mode
    public static final String PAGE_MODE = "pageMode";
    public static final String ABOUT_PAGE_MODE = "1";
    public static final String BLOG_PAGE_MODE = "2";
    public static final String INQUIRY_PAGE_MODE = "3";


    // product status

    public enum ProductStatus {
        pending(1, "pending"), available(2, "available"), banded(3, "banded"), soldOut(4, "soldOut"), sale(5, "sale");
        private int code;
        private String codeName;

        private ProductStatus(int code, String codeName) {
            this.code = code;
            this.codeName = codeName;
        }

        public int code() {
            return code;
        }

        public String codeName() {
            return codeName;
        }

    }


}
