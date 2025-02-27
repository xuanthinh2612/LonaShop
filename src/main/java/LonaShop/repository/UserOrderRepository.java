package LonaShop.repository;

import LonaShop.model.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {

    List<UserOrder> findAllByStatus(int status);
    UserOrder findByIdAndOrderCode(Long id, String orderCode);
}
