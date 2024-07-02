package com.jhkwak.productservice.repository;

import com.jhkwak.productservice.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findByUserId(Long userId);

    Optional<WishList> findByUserIdAndProductId(Long userId, Long productId);
}
