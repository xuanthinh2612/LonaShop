package LonaShop.repository;

import LonaShop.model.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {

    List<UserOrder> findAllByStatus(int status);

    @Query(value = "select * from user_order where status = 1 and payment_status = 2 or status = 1 and payment_type = 3;", nativeQuery = true)
    List<UserOrder> findWaitForConfirmList();

    List<UserOrder> findAllByStatusAndPaymentStatus(int status, int PaymentStatus);

    UserOrder findByIdAndOrderCode(Long id, String orderCode);
}
