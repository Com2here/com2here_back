package com.com2here.com2hereback.controller;

import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.common.CMResponse;
import com.com2here.com2hereback.dto.ProductListResponseDto;
import com.com2here.com2hereback.dto.ProductShowResponseDto;
import com.com2here.com2hereback.service.ProductService;
import com.com2here.com2hereback.vo.ProductListResponseVo;
import com.com2here.com2hereback.vo.ProductShowResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // 상품 조회
    @GetMapping("/show/{product_id}")
    public CMResponse<ProductShowResponseVo> showProduct(@PathVariable("product_id") Long productId) {
        try {
            ProductShowResponseDto product = productService.showProduct(productId);
            ProductShowResponseVo productShowResponseVo = ProductShowResponseVo.dtoToVo(product);
            return CMResponse.success(BaseResponseStatus.SUCCESS, productShowResponseVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 상품 리스트 조회
    @GetMapping("/list")
    public CMResponse<ProductListResponseVo> listProduct(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int limit) {
        try {
            ProductListResponseDto productListResponseDto = productService.listProduct(page, limit);
            ProductListResponseVo productListResponseVo = ProductListResponseVo.dtoToVo(productListResponseDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, productListResponseVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 관심상품 추가
    @PostMapping("/wish/add/{product_id}")
    public CMResponse<Void> addWishProduct(@PathVariable("product_id") Long productId) {
        try {
            productService.addWishProduct(productId);
            return CMResponse.success(BaseResponseStatus.SUCCESS);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 관심상품 리스트 조회
    @GetMapping("/wish/list")
    public CMResponse<ProductListResponseVo> wishlistProduct(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "limit", defaultValue = "10") int limit) {
        try {
            ProductListResponseDto productListResponseDto =  productService.wishlistProduct(page, limit);
            ProductListResponseVo productListResponseVo = ProductListResponseVo.dtoToVo(productListResponseDto);
            return CMResponse.success(BaseResponseStatus.SUCCESS, productListResponseVo);
        } catch (BaseException e) {
            return CMResponse.fail(e.getErrorCode());
        }
        catch (Exception e) {
            return CMResponse.fail(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
