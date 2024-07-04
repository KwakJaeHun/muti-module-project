package com.jhkwak.productservice.repository;

import com.jhkwak.productservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserIdAndProductId(Long userId, Long id);

    List<Cart> findByUserId(Long userId);

    List<Cart> findByUserIdAndProductIdIn(Long userId, List<Long> productIds);
}
