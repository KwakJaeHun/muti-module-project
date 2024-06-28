package com.jhkwak.orderservice.repository.user;

import com.jhkwak.orderservice.dto.user.WishListResponseDto;
import com.jhkwak.orderservice.entity.user.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    WishList findByUserIdAndProductId(Long userId, Long productId);

    // @Query(value =
    //         "SELECT p.name as productName, p.price as productPrice " +
    //         "FROM user as u " +
    //         "JOIN wish_list as w ON u.id = w.user_id " +
    //         "JOIN product as p ON p.id = w.product_id " +
    //         "WHERE u.id = :userId",
    //         nativeQuery = true
    // )
    // List<Map<String, Object>> findUserWishList(@Param("userId") Long userId);
}
