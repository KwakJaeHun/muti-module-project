package com.jhkwak.productservice.service;

import com.jhkwak.productservice.dto.*;
import com.jhkwak.productservice.entity.Product;
import com.jhkwak.productservice.entity.ProductStock;
import com.jhkwak.productservice.repository.ProductRepository;
import com.jhkwak.productservice.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;

    // 상품 전체 리스트
    public List<ProductResponseDto> productListAll() {
        return productRepository.findAll().stream().map(ProductResponseDto::new).toList();
    }

    // product_id 한개로 한개의 상품 조회
    public ProductResponseDto productList(ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(productRequestDto.getProductId()).orElseThrow(() -> new IllegalArgumentException("Not registered Product - Please try again"));
        return new ProductResponseDto(product);
    }

    // product_id 여러개로 여러개의 list 조회
    public List<ProductResponseDto> productListMany(ProductListRequestDto productListRequestDto) {
        return productRepository.findByIdIn(productListRequestDto.getProductIds()).stream().map(ProductResponseDto::new).toList();
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
    
    // 재고 업데이트
    @Transactional
    public void stockUpdate(CartRequestDto cartRequestDto) {
        if(!cartRequestDto.getProductId().isEmpty()){
            for (int i = 0; i < cartRequestDto.getProductId().size(); i++) {

                Optional<ProductStock> productStockCheck = productStockRepository.findByProductId(cartRequestDto.getProductId().get(i));

                if(productStockCheck.isPresent()){

                    ProductStock productStock = productStockCheck.get();

                    // 재고를 빼야할때
                    if(cartRequestDto.getType().equals("minus")) {
                        if (productStock.getStockQuantity() - cartRequestDto.getQuantity().get(i) > 0) {
                            productStock.setStockQuantity(productStock.getStockQuantity() - cartRequestDto.getQuantity().get(i));
                        } else {
                            productStock.setStockQuantity(0);
                        }
                    }
                    else if(cartRequestDto.getType().equals("plus")){
                        productStock.setStockQuantity(productStock.getStockQuantity() + cartRequestDto.getQuantity().get(i));
                    }
                }
            }
        }
    }
}
