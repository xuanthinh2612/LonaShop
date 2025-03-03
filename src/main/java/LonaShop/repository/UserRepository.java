package LonaShop.repository;

import LonaShop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByEmailOrPhoneNumber(String email, String phoneNumber);

    User findByPhoneNumber(String phoneNumber);
}
