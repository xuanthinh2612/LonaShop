package LonaShop.repository;

import LonaShop.model.Cover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoverRepository extends JpaRepository<Cover, Long> {

    @Query(value = "SELECT * FROM cover WHERE status = 2", nativeQuery = true)
    List<Cover> getMainCoverNum();

    @Query(value = "SELECT * FROM cover WHERE status = 1", nativeQuery = true)
    List<Cover> getSubCoverNum();
}
