package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.ProductListRespDto;
import com.com2here.com2hereback.dto.ProductShowRespDto;
import com.com2here.com2hereback.service.ProductService;
import com.com2here.com2hereback.vo.ProductListVO;
import com.com2here.com2hereback.vo.ProductShowVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/show/{product_id}")
    public CMResponse<ProductShowVO> showProduct(@PathVariable("product_id") Long productId) {
        ProductShowRespDto product = productService.showProduct(productId);
        ProductShowVO productShowVo = ProductShowVO.from(product);
        return CMResponse.success(BaseResponseStatus.SUCCESS, productShowVo);
    }

    @GetMapping("/list")
    public CMResponse<ProductListVO> listProduct(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int limit) {
        ProductListRespDto productListRespDto = productService.listProduct(page, limit);
        ProductListVO productListVo = ProductListVO.from(productListRespDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS, productListVo);
    }

    @PostMapping("/wish/add/{product_id}")
    public CMResponse<Void> addWishProduct(@PathVariable("product_id") Long productId) {
        productService.addWishProduct(productId);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }

    @GetMapping("/wish/list")
    public CMResponse<ProductListVO> wishlistProduct(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int limit) {
        ProductListRespDto productListRespDto =  productService.wishlistProduct(page, limit);
        ProductListVO productListVo = ProductListVO.from(productListRespDto);
        return CMResponse.success(BaseResponseStatus.SUCCESS, productListVo);
    }

    @DeleteMapping("/wish/delete/{wish_id}")
    public CMResponse<Void> deleteWishProduct(@PathVariable("wish_id") Long wishId) {
        productService.deleteWishProduct(wishId);
        return CMResponse.success(BaseResponseStatus.SUCCESS);
    }
}
