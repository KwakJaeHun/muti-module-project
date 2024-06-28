package com.jhkwak.orderservice.repository.user;

import com.jhkwak.orderservice.entity.user.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserIdAndProductId(Long id, Long productId);

    void deleteByUserIdAndProductId(Long userId, Long productId);

    void deleteByUserIdAndProductIdIn(Long userId, List<Long> productIds);
}
