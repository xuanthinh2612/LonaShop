package LonaShop.service;

import LonaShop.model.UserOrder;

import java.util.List;

public interface UserOrderService extends CommonService<UserOrder>{
    List<UserOrder> findAllByStatus(int status);
    List<UserOrder> findWaitForConfirmList();
    List<UserOrder> findAllByStatusAndPaymentStatus(int status, int paymentStatus);
    UserOrder findByIdAndOrderCode(Long id, String orderCode);
}
