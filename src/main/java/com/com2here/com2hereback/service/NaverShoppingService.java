package com.com2here.com2hereback.service;

import com.com2here.com2hereback.dto.ProductRespDto;
import java.util.List;

public interface NaverShoppingService {
    public List<ProductRespDto> searchFilteredProducts(String query, int budget);
}
