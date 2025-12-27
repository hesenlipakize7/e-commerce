package eCommerce.repository;

import eCommerce.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    List<Favorite> findByUserId(Long userId);
    boolean existsByUserIdAndProductId(Long userId,Long productId);
    void deleteByUserIdAndProductId(Long userId,Long productId);
}
