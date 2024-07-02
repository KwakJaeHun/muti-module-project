package com.jhkwak.productservice.service;

import com.jhkwak.productservice.dto.ProductDetailResponseDto;
import com.jhkwak.productservice.dto.ProductRegRequestDto;
import com.jhkwak.productservice.dto.ProductResponseDto;
import com.jhkwak.productservice.entity.Product;
import com.jhkwak.productservice.entity.ProductStock;
import com.jhkwak.productservice.repository.CartRepository;
import com.jhkwak.productservice.repository.ProductRepository;
import com.jhkwak.productservice.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final WishListRepository wishListRepository;
    private final CartRepository cartRepository;

    // 상품 전체 리스트
    public List<ProductResponseDto> productList() {
        return productRepository.findAll().stream().map(ProductResponseDto::new).toList();
    }
    
    // 상품 상세 정보
    public List<ProductDetailResponseDto> productDetail(Long productId) {
        return productRepository.findById(productId).stream().map(ProductDetailResponseDto::new).toList();
    }
        
    // 상품 등록
    public Product productRegistration(ProductRegRequestDto productRegRequestDto) {
        
        // 상품 정보 저장
        String name                 = productRegRequestDto.getName();
        Long price                  = productRegRequestDto.getPrice();
        String description          = productRegRequestDto.getDescription();
        String descriptionImage     = productRegRequestDto.getDescription();
        String thumbnailImage       = productRegRequestDto.getThumbnailImage();
        Character soldOutStatus     = productRegRequestDto.getSoldOutStatus();
        Character newStatus         = productRegRequestDto.getNewStatus();
        Character bestStatus        = productRegRequestDto.getBestStatus();

        // 상품 재고 저장
        int stockQuantity = productRegRequestDto.getStockQuantity();

        Product product = new Product(name, price, description, descriptionImage, thumbnailImage, soldOutStatus, newStatus, bestStatus);
        ProductStock productStock = new ProductStock(stockQuantity);
        product.setProductStock(productStock);

        return productRepository.save(product);

    }

}
