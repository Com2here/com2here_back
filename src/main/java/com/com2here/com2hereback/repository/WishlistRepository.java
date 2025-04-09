package com.com2here.com2hereback.repository;

import com.com2here.com2hereback.domain.Product;
import com.com2here.com2hereback.domain.User;
import com.com2here.com2hereback.domain.Wishlist;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    @Query("SELECT w.product FROM Wishlist w WHERE w.user = :user")
    Page<Product> findProductsByUser(@Param("user") User user, Pageable pageable);
}
