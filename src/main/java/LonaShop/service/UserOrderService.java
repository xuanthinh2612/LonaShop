package LonaShop.service;

import LonaShop.model.UserOrder;

import java.util.List;

public interface UserOrderService extends CommonService<UserOrder>{
    List<UserOrder> findAllByStatus(int status);
    UserOrder findByIdAndOrderCode(Long id, String orderCode);
}
