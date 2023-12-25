package LonaShop.repository;

import LonaShop.model.Cover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverRepository extends JpaRepository<Cover, Long> { }
