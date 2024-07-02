package com.jhkwak.productservice.repository;

import com.jhkwak.productservice.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
}
