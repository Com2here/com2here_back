package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ProductResponseDto;
import java.util.List;

public interface NaverShoppingService {
    public List<ProductResponseDto> searchFilteredProducts(String query, int budget);
}
