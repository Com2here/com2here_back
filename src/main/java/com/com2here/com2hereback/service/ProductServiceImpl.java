package com.com2here.com2hereback.service;

import com.com2here.com2hereback.common.BaseException;
import com.com2here.com2hereback.common.BaseResponseStatus;
import com.com2here.com2hereback.domain.Product;
import com.com2here.com2hereback.domain.Spec;
import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.domain.Wishlist;
import com.com2here.com2hereback.dto.ProductListRespDto;
import com.com2here.com2hereback.dto.ProductShowRespDto;
import com.com2here.com2hereback.dto.WishlistAddReqDto;
import com.com2here.com2hereback.repository.ProductRepository;
import com.com2here.com2hereback.repository.SpecRepository;
import com.com2here.com2hereback.repository.UserRepository;
import com.com2here.com2hereback.repository.WishlistRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final SpecRepository specRepository;

    @Override
    @Transactional
    public void addWishProduct(WishlistAddReqDto wishlistAddReqDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        if (wishlistRepository.existsByUserAndProduct_NaverProductId(user, wishlistAddReqDto.getNaverProductId())) {
            throw new BaseException(BaseResponseStatus.ALREADY_WISHLISTED);
        }

        Product product = productRepository.findByNaverProductId(wishlistAddReqDto.getNaverProductId())
                .orElseGet(() -> {
                    Spec spec = Spec.builder()
                            .cpu(wishlistAddReqDto.getCpu())
                            .gpu(wishlistAddReqDto.getGpu())
                            .build();
                    specRepository.save(spec);

                    Product newProduct = Product.builder()
                            .naverProductId(wishlistAddReqDto.getNaverProductId())
                            .title(wishlistAddReqDto.getTitle())
                            .image(wishlistAddReqDto.getImage())
                            .line(wishlistAddReqDto.getLine())
                            .link(wishlistAddReqDto.getLink())
                            .mall(wishlistAddReqDto.getMall())
                            .spec(spec)
                            .price(wishlistAddReqDto.getPrice())
                            .totalPrice(wishlistAddReqDto.getTotalPrice())
                            .totalScores(wishlistAddReqDto.getTotalScores())
                            .build();
                    return productRepository.save(newProduct);
                });

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .build();

        wishlistRepository.save(wishlist);
    }


    @Override
    public ProductShowRespDto showProduct(Long productId) {

        if(productId == null) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BaseException(
                BaseResponseStatus.NO_EXIST_PRODUCT));

        ProductShowRespDto productShowResponseDto = ProductShowRespDto.entityToDto(product);
        return productShowResponseDto;
    }

    @Override
    public ProductListRespDto listProduct(int page, int limit) {
        if(page == 0 || limit == 0) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Product> productPage = productRepository.findAll(pageable);

        if (productPage.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_PRODUCT);
        }

        ProductListRespDto productListResponseDto = ProductListRespDto.entityToDto(
            productPage,
            page
        );

        return productListResponseDto;
    }

    @Override
    public ProductListRespDto wishlistProduct(int page, int limit) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = String.valueOf(UUID.fromString(authentication.getName()));

        if(page == 0 || limit == 0) {
            throw new BaseException(BaseResponseStatus.WRONG_PARAM);
        }

        User user = userRepository.findByUuid(uuid);

        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Product> productPage = wishlistRepository.findProductsByUser(user, pageable);

        if (productPage == null || productPage.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NO_PRODUCT_FOUND);
        }

        ProductListRespDto productListResponseDto = ProductListRespDto.entityToDto(
            productPage,
            page
        );

        return productListResponseDto;
    }

    @Override
    @Transactional
    public void deleteWishProduct(Long naverProductId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uuid = (String) authentication.getPrincipal();

        User user = userRepository.findByUuid(uuid);
        if (user == null) {
            throw new BaseException(BaseResponseStatus.NO_EXIST_MEMBERS);
        }

        Wishlist wishlist = wishlistRepository.findByUserAndProduct_NaverProductId(user, naverProductId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_EXIST_WISH));

        if (!wishlist.getUser().equals(user)) {
        throw new BaseException(BaseResponseStatus.NO_PERMISSION_WISH);
    }

        wishlistRepository.delete(wishlist);
    }
}
