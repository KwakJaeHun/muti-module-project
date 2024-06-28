package com.jhkwak.userservice.service.product;

import com.jhkwak.userservice.dto.product.ProductDetailResponseDto;
import com.jhkwak.userservice.dto.product.ProductRegRequestDto;
import com.jhkwak.userservice.dto.product.ProductResponseDto;
import com.jhkwak.userservice.dto.product.WishRequestDto;
import com.jhkwak.userservice.entity.product.Product;
import com.jhkwak.userservice.entity.product.ProductStock;
import com.jhkwak.userservice.entity.user.User;
import com.jhkwak.userservice.entity.user.WishList;
import com.jhkwak.userservice.repository.product.ProductRepository;
import com.jhkwak.userservice.repository.user.UserRepository;
import com.jhkwak.userservice.repository.user.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishListRepository wishListRepository;
    
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

    public void wishAdd(User user, WishRequestDto wishRequestDto) {
        Product product = productRepository.findById(wishRequestDto.getProductId()).get();

        WishList wishList = new WishList(user, product);
        wishListRepository.save(wishList);
    }
}
