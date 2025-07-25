package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ProductListRespDto;
import com.com2here.com2hereback.dto.ProductShowRespDto;

public interface ProductService {
    void addWishProduct(Long productId);
    ProductShowRespDto showProduct(Long productId);
    ProductListRespDto listProduct (int page, int limit);
    ProductListRespDto wishlistProduct (int page, int limit);
}
