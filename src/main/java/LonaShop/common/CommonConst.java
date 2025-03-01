package LonaShop.common;

public class CommonConst {

    //    cover status
    public static final int MAIN_COVER = 2;
    public static final int SUB_COVER = 1;
    public static final int DISABLED = 0;

    // common flag
    public static final int FLAG_ON = 1;
    public static final int FLAG_OFF = 0;


    // order status
    public static final String ORDER_STATUS = "orderStatus";
    public static final int ORDERED_STATUS = 1;
    public static final int ADMIN_CONFIRMED_STATUS = 2;
    public static final int PACKING_STATUS = 3;
    public static final int DELIVERED_STATUS = 4;
    public static final int USER_CANCELED_STATUS = 5;
    public static final int DENIED_STATUS = 6;
    public static final int RETURN_STATUS = 7;

    // page mode
    public static final String PAGE_MODE = "pageMode";
    public static final String HOME_PAGE_MODE = "0";
    public static final String ABOUT_PAGE_MODE = "1";
    public static final String BLOG_PAGE_MODE = "2";
    public static final String INQUIRY_PAGE_MODE = "3";
    public static final String SEARCH_MODE = "4";

    // payment type
    public static final int PAY_BY_MOMO = 1;
    public static final int PAY_BY_BANK_TRANSFER = 2;
    public static final int PAY_WHEN_RECEIVE = 3;

    // payment status
    public static final int NOT_PAY = 0;
    public static final int WAITING_FOR_PAY = 1;
    public static final int USER_CONFIRMED_PAY = 2;
    public static final int ADMIN_CONFIRMED_PAID = 3;

    // User type order
    public static final String USER_ORDER = "UOD";
    public static final String NON_USER_ORDER = "NOUOD";



    // product status

    public enum ProductStatus {
        pending(1, "pending"),
        available(2, "available"),
        banded(3, "banded"),
        soldOut(4, "soldOut"),
        sale(5, "sale");
        private final int code;
        private final String codeName;

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
