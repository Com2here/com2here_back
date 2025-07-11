package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ProductListResponseDto;
import com.com2here.com2hereback.dto.ProductShowResponseDto;

public interface ProductService {
    void addWishProduct(Long productId);
    ProductShowResponseDto showProduct(Long productId);
    ProductListResponseDto listProduct (int page, int limit);
    ProductListResponseDto wishlistProduct (int page, int limit);
}
