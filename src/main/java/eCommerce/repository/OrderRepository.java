package eCommerce.repository;

import eCommerce.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAllByUserId(Long userId);
    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}
