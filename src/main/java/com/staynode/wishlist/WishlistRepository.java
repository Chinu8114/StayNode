package com.staynode.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Wishlist.PK> {
    List<Wishlist> findByUserId(String userId);
}
