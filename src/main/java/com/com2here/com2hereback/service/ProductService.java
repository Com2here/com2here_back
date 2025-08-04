package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ProductListRespDto;
import com.com2here.com2hereback.dto.ProductShowRespDto;
import com.com2here.com2hereback.dto.WishlistAddReqDto;

public interface ProductService {
    void addWishProduct(WishlistAddReqDto wishlistAddReqDto);
    ProductShowRespDto showProduct(Long productId);
    ProductListRespDto listProduct (int page, int limit);
    ProductListRespDto wishlistProduct (int page, int limit);
    void deleteWishProduct(Long naverProductId);
}
